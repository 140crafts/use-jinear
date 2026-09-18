# Jinear MCP Sunucusu - Tam Uç Testi Raporu

**Tarih:** 2026-09-12
**Instance:** https://jinear.cagdastunca.com (test instance)
**MCP endpoint:** https://api.jinear.cagdastunca.com/mcp
**Kapsam:** MCP üzerinden expose edilen 25 tool'un tamamı
**Yöntem:** Her tool en az bir kez çağrıldı. Yazma yapan uçların sonucu ayrıca `get_task` / `list_*` ile doğrulandı.

---

## 1. Özet

| Durum | Adet |
|---|---|
| Çalışıyor | 18 |
| Bug bulundu | 6 |
| Veri olmadığı için sadece boş-liste davranışı doğrulandı | 3 |

**Öncelik sırası:**

1. **P0** - Arama indeksi yeni oluşturulan kayıtları görmüyor
2. **P1** - `fetch` tool'u hiçbir geçerli girdi kabul etmiyor (`search` ile sözleşme çelişkisi)
3. **P1** - `get_file_link` varlık/tip doğrulaması yapmıyor, yetki sorusu doğuruyor
4. **P2** - `set_task_status` ve `create_task` yanıtlarında statü alanları stale/null
5. **P3** - `list_task_comments` sıralaması dokümante edilenin tersi
6. **P3** - `quoteCommentId` yazılıyor mu doğrulanamıyor, response'ta alan yok
7. **Takip** - `list_tasks` üzerinde tekrarlanamayan geçici 500

---

## 2. Test ortamı

Tüm testlerde kullanılan id'ler:

```
workspaceId          01m29431cxmzxf890mhmgz6xjy   (username: admin37, tier: PRO)
teamId               01m29431ns5fshtx88qg11k5ch   (tag: adm)
accountId            01m29430ea8m8zp0qc01fqa8se   (username: admin, role OWNER)
notebookId           01m29432an1a7webabcpp3cysz   (title: admin37)

workflow statuses (team adm):
  01m2943234vtbntyvbqpm3h51c  Not Started   NOT_STARTED
  01m294320d89k2rkawx0qtpjcb  Backlog       BACKLOG
  01m294326hv5pdbx83gx78934x  Started       STARTED
  01m294328br0y8s0syvkar7xfv  Completed     COMPLETED
  01m294329rrvw6a4q0v4csmcj7  Cancelled     CANCELLED

test sırasında oluşturulanlar:
  taskBoardId        01m2as5zvbf61nvxstatsd2kap   "MCP Smoke Test"
  taskId (adm-6)     01m2as66aj9ssc9ft6c30224e5   "MCP tam parametre testi"
  commentId          01m2as6nr6krnhkbjwdwmgtcgp   (ilk yorum)
  commentId          01m2as6y2wps67x94q3ejetr6x   (quote'lu yanıt)
```

---

## 3. Bulgular

### BUG-1 (P0) - Arama indeksi yeni kayıtları içermiyor

`create_task` ile oluşturulan adm-6, ne `search_tasks` ne de global `search` ile bulunabiliyor. Aynı anda daha eski kayıtlar sorunsuz dönüyor. Yani indeks çalışıyor ama create/update path'inde yazılmıyor veya refresh tetiklenmiyor.

**Repro:**

```
# adm-6 başlığı: "MCP tam parametre testi (guncellendi)"

search_tasks { workspaceId, query: "parametre" }
-> {"items":[],"totalElements":0}

search_tasks { workspaceId, query: "MCP" }
-> yalnızca adm-1 "hello mcp from claude" döner, adm-6 dönmez

search { query: "MCP tam parametre testi" }
-> {"results":[]}

# kontrol: aynı anda eski kayıtlar bulunuyor
search_tasks { workspaceId, query: "osman" }
-> adm-2, adm-3, adm-4, adm-5 (4 kayıt)
```

**İkincil gözlem, aynı uçta:** eşleşme davranışı substring değil. `"parametre"` başlığın ortasında geçtiği halde eşleşmiyor. `"a"` sorgusu adm-1'i getiriyor, `"e"` hiçbir şey getirmiyor. Muhtemelen token prefix eşleşmesi ve reference alanı da indekste (`adm-1` -> `"a"` prefix'i). Kullanıcı davranışı açısından sorunlu: insanlar başlığın ortasındaki kelimeyi arıyor.

**Bakılacak yer:** task create/update servisinde indeks yazma çağrısı var mı, varsa aynı transaction içinde mi ve commit sonrası mı tetikleniyor. Eğer async ise event publish edilip edilmediği. Ayrıca indeks analyzer'ın prefix yerine substring/ngram olması gerekip gerekmediği ürün kararı.

---

### BUG-2 (P1) - `fetch` tool'unun geçerli girdisi yok

`search`, sonuçları `task:<ulid>` formatında id ile dönüyor ve tool açıklaması bu id'nin `fetch`'e verilmesini söylüyor. `fetch` ise task id'lerini reddediyor.

**Repro:**

```
search { query: "osman" }
-> {"results":[{"id":"task:01m2965mn6drsdy5sbqaq6jasr",
                "title":"osman",
                "url":"https://jinear.cagdastunca.com/admin37/task/adm-5"}, ...]}

fetch { id: "task:01m2965mn6drsdy5sbqaq6jasr" }
-> ERROR: "Task ids from search cannot be fetched directly.
           Use get_task with the workspace username, team tag and task number shown in the url."
```

Instance'ta not bulunmadığı için `fetch`'in kabul ettiği tek tip (note id) test edilemedi. Pratikte tool şu an tamamen kullanılamaz durumda ve `search` açıklaması yanlış yönlendiriyor.

**Karar gereken:** ya `fetch` task id'lerini de çözecek (tercih edilen, MCP tarafında search -> fetch akışı standart), ya da `search` tool açıklamasından fetch yönlendirmesi kaldırılıp url/get_task'e işaret edilecek.

---

### BUG-3 (P1) - `get_file_link` doğrulama yapmıyor

Bir notebook id'si verdim, dosya olup olmadığına bakmadan link üretti.

**Repro:**

```
get_file_link { materialId: "01m29432an1a7webabcpp3cysz" }   # bu bir notebookId, material değil
-> {"materialId":"01m29432an1a7webabcpp3cysz",
    "url":"https://api.jinear.cagdastunca.com/v1/material/media/01m29432an1a7webabcpp3cysz"}
```

Ne kaydın varlığı ne FILE/FOLDER tipi kontrol ediliyor; endpoint id'yi string olarak URL'e yapıştırıyor. Karşılaştırma: `get_note` geçersiz id'de düzgün hata veriyor:

```
get_note { workspaceId, noteId: "01m29432an1a7webabcpp3cysz" }
-> ERROR: "No note with that id is visible to you in this workspace. Check noteId against search_notes."
```

**Asıl risk burada değil:** link üretimi kontrolsüz olduğu için, `/v1/material/media/{id}` endpoint'inin kendisinde workspace/permission kontrolü olup olmadığını doğrulaman gerekiyor. Yoksa ULID bilen biri başka workspace'in dosyasını indirebilir. Canlıya çıkmadan önce bu endpoint'e yetki testi yazılmalı.

---

### BUG-4 (P2) - Statü alanları yanıtta stale veya null

`set_task_status` yanıtı `workflowStatusId`'yi güncel, `workflowStatusName` ve `workflowStateGroup`'u güncelleme öncesi değerle dönüyor. Kalıcılık doğru, sadece response hydration hatalı.

**Repro:**

```
set_task_status { taskId: 01m2as66aj9ssc9ft6c30224e5,
                  workflowStatusId: 01m294326hv5pdbx83gx78934x }   # Started
-> "workflowStatusId":"01m294326hv5pdbx83gx78934x",
   "workflowStatusName":"Backlog",          <-- yanlış
   "workflowStateGroup":"BACKLOG"           <-- yanlış

get_task { admin37, adm, 6 }
-> "workflowStatusName":"Started","workflowStateGroup":"STARTED"   <-- DB doğru

# ikinci kez, Cancelled'a taşırken de aynı:
set_task_status { ..., workflowStatusId: 01m294329rrvw6a4q0v4csmcj7 }   # Cancelled
-> "workflowStatusId":"01m294329rrvw6a4q0v4csmcj7",
   "workflowStatusName":"Started"           <-- yine bir önceki değer
```

Aynı kök neden `create_task`'te de görünüyor, orada alanlar null geliyor:

```
create_task { ... }
-> "workflowStatusId":"01m294320d89k2rkawx0qtpjcb",
   "workflowStatusName":null,
   "workflowStateGroup":null
```

**Bakılacak yer:** statü set edildikten sonra entity'nin lazy status ilişkisi refresh edilmeden DTO'ya map'leniyor. Save sonrası entity'yi yeniden okumak veya status nesnesini elle set etmek yeterli.

---

### BUG-5 (P3) - `list_task_comments` sıralaması ters

Tool açıklaması "newest first" diyor, uç en eskiyi önce döndürüyor.

```
list_task_comments { taskId: 01m2as66aj9ssc9ft6c30224e5 }
-> [0] 01m2as6nr6krnhkbjwdwmgtcgp  createdAt 2026-09-12T12:24:16.390Z
   [1] 01m2as6y2wps67x94q3ejetr6x  createdAt 2026-09-12T12:24:24.924Z
```

Ya sort yönü düzeltilecek ya da açıklama. Sayfalama ile birlikte düşünülmeli: yanlış yön, ikinci sayfayı isteyen bir istemcide sessizce yanlış veri verir.

---

### BUG-6 (P3) - `quoteCommentId` doğrulanamıyor

Quote'lu yorum `ok: true` dönüyor ama ilişkinin yazılıp yazılmadığı MCP üzerinden görülemiyor. Ne `add_task_comment` yanıtında ne de `list_task_comments` çıktısındaki yorum nesnesinde `quoteCommentId` alanı yok.

```
add_task_comment { taskId: 01m2as66aj9ssc9ft6c30224e5,
                   comment: "Bu bir yanit testi, quoteCommentId ile gonderiliyor.",
                   quoteCommentId: "01m2as6nr6krnhkbjwdwmgtcgp" }
-> {"taskId":"01m2as66aj9ssc9ft6c30224e5","ok":true}

list_task_comments -> dönen yorum nesnesi:
   {"commentId","taskId","authorAccountId","authorUsername","body","createdAt"}
   quote alanı yok
```

Comment DTO'suna `quoteCommentId` eklenmeli. Ayrıca `add_task_comment` yanıtının oluşturulan `commentId`'yi dönmesi iyi olur: şu an bir yoruma yanıt vermek için önce yorum atıp sonra `list_task_comments` çağırmak gerekiyor, gereksiz round-trip.

---

### TAKIP-1 - `list_tasks` üzerinde tekrarlanamayan hata

Oturumun başında `list_tasks`'e yapılan 5 ardışık çağrının tamamı, hiç parametre almayan varyant dahil, şu hatayı döndürdü:

```
list_tasks { workspaceId }                                  -> ERROR
list_tasks { workspaceId, assigneeIds:[...] }               -> ERROR
list_tasks { workspaceId, assigneeIds, stateGroups, pageSize } -> ERROR
list_tasks { workspaceId, teamIds, pageSize }               -> ERROR
list_tasks { workspaceId, teamIds, stateGroups, page, pageSize } -> ERROR

hata metni: "The request could not be completed. Try again, or narrow the arguments."
```

Aynı çağrılar birkaç dakika sonra sorunsuz çalıştı. Geçici bir DB bağlantısı / connection pool tükenmesi olabilir. O zaman aralığının server loglarına bakılması gerekiyor.

**Ayrıca:** bu hata metni jenerik ve debug edilemez. Sunucu tarafında gerçek exception loglanıp yanıtta bir correlation id dönmesi, ileride bu tip vakaları takip etmeyi çok kolaylaştırır.

---

## 4. Tam çağrı logu

Kronolojik. Uzun yanıtlar `[...]` ile kısaltıldı, kritik alanlar korundu.

### 4.1 Keşif

```
list_workspaces {}
-> {"items":[{"workspaceId":"01m29431cxmzxf890mhmgz6xjy","username":"admin37",
              "title":"admin","tier":"PRO","role":"OWNER"}],"count":1}                    OK

list_workspace_members { workspaceId }
-> {"items":[{"workspaceMemberId":"01m29431jxkn36bd8cbypj9dv8",
              "accountId":"01m29430ea8m8zp0qc01fqa8se","role":"OWNER",
              "username":"admin","email":"admin@jinear.cagdastunca.com"}],
    "page":0,"pageSize":250,"totalElements":1}                                            OK

list_teams { workspaceId }
-> {"items":[{"teamId":"01m29431ns5fshtx88qg11k5ch","name":"admin","username":"adm",
              "tag":"adm","taskVisibility":"VISIBLE_TO_ALL_TEAM_MEMBERS",
              "teamState":"ACTIVE"}],"count":1}                                           OK

get_workspace { username: "admin37" }
-> {"workspace":{"workspaceId":"01m29431cxmzxf890mhmgz6xjy","username":"admin37",
                 "title":"admin","tier":"PRO"}}                                           OK

get_workspace { workspaceId: "01m29431cxmzxf890mhmgz6xjy" }
-> aynı yanıt                                                                             OK

list_workflow_statuses { teamId }
-> 5 statü (bkz. bölüm 2)                                                                 OK

list_topics { teamId }
-> {"items":[],"totalElements":0}                                            OK (veri yok)
```

### 4.2 İlk `list_tasks` denemeleri - hepsi hata

```
list_tasks { workspaceId, assigneeIds:["01m29430ea8m8zp0qc01fqa8se"],
             stateGroups:["BACKLOG","NOT_STARTED","STARTED"], pageSize:50 }
-> ERROR "The request could not be completed. Try again, or narrow the arguments."

list_tasks { workspaceId, assigneeIds:[...], pageSize:50 }              -> ERROR (aynı)
list_tasks { workspaceId }                                             -> ERROR (aynı)
list_tasks { workspaceId, teamIds:[...], pageSize:50 }                 -> ERROR (aynı)
list_tasks { workspaceId, teamIds:[...], stateGroups:["BACKLOG"],
             page:0, pageSize:20 }                                     -> ERROR (aynı)
```

Bkz. TAKIP-1.

### 4.3 `list_tasks` sonraki denemeler - hepsi başarılı

```
list_tasks { workspaceId }
-> 5 kayıt: adm-5 (Started), adm-4, adm-3, adm-2 (Backlog), adm-1 (Backlog)
   "page":0,"pageSize":20,"totalElements":5,"hasNext":false                               OK

list_tasks { workspaceId, assigneeIds:["01m29430ea8m8zp0qc01fqa8se"],
             stateGroups:["STARTED"] }
-> adm-6, adm-5 (2 kayıt)                                              OK (filtre doğru)

list_tasks { workspaceId, from:"2026-09-24T00:00:00Z", to:"2026-09-26T00:00:00Z" }
-> adm-6 (dueDate 2026-09-25)                                          OK (tarih filtresi doğru)
```

### 4.4 Okuma uçları

```
get_task { workspaceUsername:"admin37", teamTag:"adm", taskNumber:1 }
-> {"task":{"taskId":"01m294h75f40dwc97q1wpcnce8","reference":"adm-1",
            "title":"hello mcp from claude","workflowStatusName":"Backlog",
            "description":null, [...]}}                                                   OK

get_task { admin37, adm, 999 }
-> ERROR "No such record. Check the id and try again."                     OK (doğru hata)

list_calendar_events { workspaceId, from:"2026-09-01T00:00:00Z", to:"2026-09-30T23:59:59Z" }
-> 2 event, ikisi de sourceType TASK, calendarId null                                     OK

list_calendar_events { ..., teamIds:["01m29431ns5fshtx88qg11k5ch"] }
-> 3 event (adm-6 eklendikten sonra), tarihleri güncel                 OK (filtre doğru)
   not: adm-6 startsAt 2026-09-18 / endsAt 2026-09-25, update_task ile set edilen
   tarih çifti doğru yansımış

list_notebooks { workspaceId }
-> {"items":[{"notebookId":"01m29432an1a7webabcpp3cysz","title":"admin37",
              "visibility":"PUBLIC_WITHIN_WORKSPACE"}],"totalElements":1}                 OK

search_notes { workspaceId }
-> {"items":[],"totalElements":0}                                            OK (veri yok)

search_notes { workspaceId, notebookId:"01m29432an1a7webabcpp3cysz", titleContains:"test" }
-> {"items":[],"totalElements":0}                                            OK (veri yok)

get_note { workspaceId, noteId:"01m29432an1a7webabcpp3cysz" }
-> ERROR "No note with that id is visible to you in this workspace."       OK (doğru hata)

list_files { workspaceId }
-> {"items":[],"totalElements":0}                                            OK (veri yok)

get_file_link { materialId:"01m29432an1a7webabcpp3cysz" }
-> link üretti                                                                       BUG-3
```

### 4.5 Arama

```
search { query:"osman" }
-> 4 sonuç, id formatı "task:<ulid>", url "https://jinear.cagdastunca.com/admin37/task/adm-N"  OK

fetch { id:"task:01m2965mn6drsdy5sbqaq6jasr" }
-> ERROR                                                                             BUG-2

search_tasks { workspaceId, query:"a" }      -> adm-1 (1 kayıt)
search_tasks { workspaceId, query:"e" }      -> 0 kayıt                    tuhaf, bkz BUG-1
search_tasks { workspaceId, query:"osman" }  -> adm-2,3,4,5 (4 kayıt)                     OK
search_tasks { workspaceId, query:"MCP" }    -> yalnızca adm-1                       BUG-1
search_tasks { workspaceId, query:"parametre",
               teamIds:["01m29431ns5fshtx88qg11k5ch"] } -> 0 kayıt                   BUG-1
search { query:"MCP tam parametre testi" }   -> {"results":[]}                       BUG-1
```

### 4.6 Yazma uçları

```
create_task_board { workspaceId, teamId, title:"MCP Smoke Test",
                    dueDate:"2026-09-30T17:00:00Z" }
-> {"board":{"taskBoardId":"01m2as5zvbf61nvxstatsd2kap","title":"MCP Smoke Test",
             "state":"OPEN","dueDate":"2026-09-30T17:00:00Z"}}                            OK

list_task_boards { workspaceId, teamId }   # create öncesi
-> {"items":[],"totalElements":0}                                                         OK
list_task_boards { workspaceId, teamId }   # create sonrası
-> 1 board, alanlar doğru                                                                 OK

create_task { workspaceId, teamId, title:"MCP tam parametre testi",
              description:"... &lt;b&gt;basit HTML&lt;/b&gt; ...",
              assignedTo:"01m29430ea8m8zp0qc01fqa8se",
              boardId:"01m2as5zvbf61nvxstatsd2kap",
              startDate:"2026-09-15T09:00:00Z", dueDate:"2026-09-20T15:00:00Z" }
-> {"task":{"taskId":"01m2as66aj9ssc9ft6c30224e5","reference":"adm-6",
            "workflowStatusId":"01m294320d89k2rkawx0qtpjcb",
            "workflowStatusName":null,"workflowStateGroup":null,   <-- BUG-4
            "assignedDate":"2026-09-15T09:00:00Z","dueDate":"2026-09-20T15:00:00Z"}}
   not: startDate parametresi yanıtta assignedDate olarak dönüyor, bu alan
   adlandırması kasıtlı mı kontrol et                                          KISMEN OK

add_task_to_board { taskBoardId:"01m2as5zvbf61nvxstatsd2kap",
                    taskId:"01m2965mn6drsdy5sbqaq6jasr" }
-> {"taskBoardId":"01m2as5zvbf61nvxstatsd2kap","ok":true}                                 OK

add_task_to_board { taskBoardId:"01m29432an1a7webabcpp3cysz",   # gecersiz id
                    taskId:"01m2as66aj9ssc9ft6c30224e5" }
-> ERROR "No such record. Check the id and try again."                     OK (doğru hata)

add_task_comment { taskId:"01m2as66aj9ssc9ft6c30224e5",
                   comment:"MCP uzerinden atilan ilk test yorumu." }
-> {"taskId":"01m2as66aj9ssc9ft6c30224e5","ok":true}       OK (commentId dönmüyor, BUG-6)

list_task_comments { taskId:"01m2as66aj9ssc9ft6c30224e5" }
-> 1 yorum, body ve authorUsername doğru                                                  OK

add_task_comment { taskId, comment:"Bu bir yanit testi...",
                   quoteCommentId:"01m2as6nr6krnhkbjwdwmgtcgp" }
-> {"ok":true}                                                                       BUG-6

list_task_comments { taskId }
-> 2 yorum, eski->yeni sırada, quote alanı yok                              BUG-5, BUG-6

set_task_status { taskId, workflowStatusId:"01m294326hv5pdbx83gx78934x" }   # Started
-> statusId doğru, statusName "Backlog"                                              BUG-4
get_task { admin37, adm, 6 }
-> "workflowStatusName":"Started"                                        OK (DB doğru)

update_task { taskId, title:"MCP tam parametre testi (guncellendi)",
              description:"Guncellenmis govde. update_task testi.",
              startDate:"2026-09-18T09:00:00Z", dueDate:"2026-09-25T15:00:00Z" }
-> {"taskId":"...","ok":true}                                                             OK
get_task { admin37, adm, 6 }
-> title, description, assignedDate, dueDate hepsi güncel                OK (kalıcılık doğru)

update_task { taskId, assignedTo:"" }        # unassign testi
-> {"ok":true}
get_task -> "assignedTo":null                                        OK (unassign çalışıyor)

set_task_status { taskId, workflowStatusId:"01m294329rrvw6a4q0v4csmcj7" }  # Cancelled
-> statusId doğru, statusName "Started"                                              BUG-4
```

---

## 5. Test edilemeyenler

Instance'ta veri olmadığı için sadece boş-liste davranışı doğrulandı:

- `list_files` - dosya yok
- `search_notes` - not yok
- `get_note` - not yok, yalnızca hata yolu test edildi
- `get_file_link` - gerçek bir FILE materyali ile test edilmedi
- `fetch` - kabul ettiği tek tip note id, o da test edilemedi

Bunları gerçek veriyle denemek için UI'dan birkaç not ve dosya oluşturmak gerekiyor.

**Ürün notu:** MCP tarafında not veya dosya oluşturan/güncelleyen tool yok. Not tarafı tamamen salt okunur. `create_note` / `update_note` eklemek, MCP'nin kullanım alanını belirgin şekilde genişletir.

---

## 6. Instance'ta kalan test artıkları

Temizlenmesi gerekenler:

- **adm-6** `01m2as66aj9ssc9ft6c30224e5` - "MCP tam parametre testi (guncellendi)", Cancelled statüsünde, atanmamış, 2 test yorumu var
- **MCP Smoke Test** board `01m2as5zvbf61nvxstatsd2kap` - içinde adm-5 ve adm-6
- **adm-5** `01m2965mn6drsdy5sbqaq6jasr` - test sırasında Started'a alındı ve admin hesabına atandı, board'a eklendi
- **adm-2, adm-3, adm-4** - test öncesinden gelen, birebir aynı başlıklı ("Osman ile konu") üç kayıt, muhtemelen tekrarlı üretim. Nereden geldiklerine bakmaya değer.

---

## 7. Claude Code için öneri sıra

1. BUG-1'i çöz: task create/update path'inde indeks yazımını doğrula, entegrasyon testi ekle (task oluştur, hemen ara, bulunmalı).
2. BUG-3 kapsamında `/v1/material/media/{id}` endpoint'ine yetki testi yaz. Yetki kontrolü yoksa bu canlıya çıkış engelidir.
3. BUG-2 için karar ver: `fetch` task id'lerini çözsün veya `search` açıklaması düzeltilsin.
4. BUG-4 için save sonrası entity refresh, DTO mapper'ı tek noktadan düzelt (create ve status update aynı kök).
5. BUG-5, BUG-6 küçük: sort yönü ve DTO alanı.
6. Jenerik hata mesajı yerine correlation id dönen bir error handler ekle, TAKIP-1 gibi vakalar tekrar yaşanırsa loglardan izlenebilsin.
