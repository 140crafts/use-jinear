import enUsLocale from "date-fns/locale/en-US";
import trTrLocale from "date-fns/locale/tr";

const translations = {
  hi: {
    en: "hi",
    tr: "selam"
  },
  locale: {
    en: "1",
    tr: "0"
  },
  localeType: {
    en: "EN",
    tr: "TR"
  },
  dateFnsLocale: {
    en: enUsLocale,
    tr: trTrLocale
  },
  dateFormat: {
    en: "MM.dd.yyyy",
    tr: "dd.MM.yyyy"
  },
  dateTimeFormat: {
    en: "MM.dd.yyyy hh:mm aaa",
    tr: "dd.MM.yyyy  HH:mm"
  },
  timeFormat: {
    en: "hh:mm aaa",
    tr: "HH:mm"
  },
  timeFormatShort: {
    en: "hh aaa",
    tr: "HH"
  },
  continue: {
    en: "Continue",
    tr: "Devam"
  },
  cancel: {
    en: "Cancel",
    tr: "Vazgeç"
  },
  genericError: {
    en: "An error has occurred please try again later",
    tr: "Bir hata oluştu. Lütfen daha sonra tekrar deneyin"
  },
  genericSuccess: {
    en: "Success",
    tr: "İşlem Başarılı"
  },
  notYetAvailable: {
    en: "Not yet available",
    tr: "Henüz aktif değil"
  },
  loginScreenTitle: {
    en: "Login",
    tr: "Giriş Yap"
  },
  loginScreenText: {
    en: "Please enter your email address",
    tr: "Lütfen e-posta adresinizi girin"
  },
  loginScreenEmailPlaceholder: {
    en: "cagdas@example.com",
    tr: "cagdas@example.com"
  },
  loginScreenCodePlaceholder: {
    en: "Your Code",
    tr: "Onay Kodun"
  },
  loginScreenCodeText: {
    en: "Please enter the code we sent you",
    tr: "Lütfen e-posta adresine gönderdiğimiz kodu gir"
  },
  loginScreenEmailSubText: {
    en: "Please provide a valid email address. We will send you a verification token.",
    tr: "Geçerli bir e-posta adresi girdiğinden emin ol. Onay kodu içeren bir e-posta alacaksın."
  },
  loginScreenCodeSubText: {
    en: "You should receive an email with neccessery info soon. Don’t forget to check your <b>Spam</b>, <b>Social</b> or <b>Promotions</b> folders!",
    tr: "Kısa bir süre içerisinde giriş bilgilerini içeren bir e-posta alacaksın. <b>Spam</b>, <b>Sosyal</b> veya <b>Promosyonlar</b> klasörlerini kontrol etmeyi unutma!"
  },
  loginScreenEmailSendCode: {
    en: "Send verification Code",
    tr: "Onay Kodu Gönder"
  },
  loginScreenCodeCancel: {
    en: "Request a new code",
    tr: "Yeni bir kod talep et"
  },
  loginScreenInvalidMailToast: {
    en: "Please provide a valid email address",
    tr: "Lütfen geçerli bir e-posta adresi girin"
  },
  loginScreenResendWait: {
    en: "Please wait ${timeout} seconds to resend an another code.",
    tr: "Yeni bir kod için lütfen ${timeout} saniye daha bekleyin."
  },
  loginButtonLabel: {
    en: "Login",
    tr: "Giriş Yap"
  },
  logoutButtonLabel: {
    en: "Logout",
    tr: "Çıkış"
  },
  welcomeBackLabel: {
    en: "Welcome back, ${username}",
    tr: "Hoşgeldin, ${username}"
  },
  notFoundModalReturnHomeButtonLabel: {
    en: "Return Home",
    tr: "Anasayfaya Dön"
  },
  notFoundModalTitle: {
    en: "Not Found",
    tr: "Bulunamadı"
  },
  loginWithEmailFormTitle: {
    en: "Sign in",
    tr: "Giriş Yap"
  },
  loginWithEmailFormSubTitle: {
    en: "Don't you have an account?",
    tr: "Hesabınız yok mu?"
  },
  loginWithEmailFormTitleLink: {
    en: "Sign up for a new account",
    tr: "Yeni bir üyelik oluştur"
  },
  loginWithEmailEmailLabel: {
    en: "Email",
    tr: "Email"
  },
  loginWithEmailPasswordLabel: {
    en: "Password",
    tr: "Şifre"
  },
  forgotPasswordLinkLabel: {
    en: "Forgot password?",
    tr: "Şifremi unuttum"
  },
  loginWithEmailLoginButtonLabel: {
    en: "Login",
    tr: "Giriş Yap"
  },
  forgotPasswordScreenBackToLogin: {
    en: "Back to Login",
    tr: "Giriş Yapmaya Dön"
  },
  or: {
    en: "or",
    tr: "veya"
  },
  loginWith2FaMail: {
    en: "Login With Email Verification",
    tr: "Email Doğrulamasıyla Giriş Yap"
  },
  loginSuccessToast: {
    en: "Login success",
    tr: "Giriş Başarılı"
  },
  registerWithMailFormTitle: {
    en: "Sign Up",
    tr: "Kayıt ol"
  },
  registerWithMailFormSubTitle: {
    en: "Already have an account?",
    tr: "Hesabınız var mı?"
  },
  registerWithMailFormSubTitleLinkLabel: {
    en: "Sign in",
    tr: "Giriş yap"
  },
  registerWithEmailEmailLabel: {
    en: "Email",
    tr: "Email"
  },
  registerWithEmailPasswordLabel: {
    en: "Password",
    tr: "Şifre"
  },
  registerWithEmailPasswordConfirmLabel: {
    en: "Password Confirmation",
    tr: "Şifre Doğrulama"
  },
  registerWithEmailPasswordRegisterLabel: {
    en: "Register",
    tr: "Kayıt Ol"
  },
  registerPrivacyPolicyText: {
    en: "By registering, you agree to the processing of your personal data by this platform as described in the",
    tr: "Kayıt olarak kişisel verilerinizin bu platform tarafından gizlilik sözleşmesi çerçevesinde işlenmesini kabul etmiş olursunuz."
  },
  privacyPolicy: {
    en: "Privacy Policy",
    tr: "Gizlilik Sözleşmesi"
  },
  registerWithEmailAgreeTerms: {
    en: "I've read and agree to the",
    tr: "Kullanıcı sözleşmesini onaylıyorum"
  },
  terms: {
    en: "Terms of Service",
    tr: "Kullanıcı Sözleşmesi"
  },
  registerWithEmailPasswordsNotMatch: {
    en: "Passwords does not match!",
    tr: "Girilen şifreler birbiri ile aynı değil!"
  },
  registerWithEmailIsSuccessfull: {
    en: "Successfully registered. Please login to your account",
    tr: "Başarıyla kayıt oldunuz. Lütfen giriş yapın"
  },
  forgotPasswordFormTitle: {
    en: "Reset your password",
    tr: "Şifreni sıfırla"
  },
  forgotPasswordEmailLabel: {
    en: "Email",
    tr: "Email"
  },
  forgotPasswordFormResetButton: {
    en: "Reset password",
    tr: "Şifreni Sıfırla"
  },
  forgotPasswordInitializeSuccess: {
    en: "We've successfuly received your request. Please check your email to reset your password.",
    tr: "İsteğini başarıyla aldık. Lüften email adresini kontrol et."
  },
  engageCompletePasswordResetErrorTitle: {
    en: "Complete password reset has failed!",
    tr: "Şifre sıfırlama başarısız!"
  },
  engageCompletePasswordResetErrorText: {
    en: "Your request might be timed out. Please go to forgot password page and resend confirmation mail.",
    tr: "İsteğiniz zaman aşımına uğramış olabilir. Lütfen şifremi unuttum sayfasına geri dönün ve yeni bir onay maili isteğinde bulunun."
  },
  engageCompletePasswordResetSuccessTitle: {
    en: "Your password successfully reset",
    tr: "Şifreniz başarıyla sıfırlandı"
  },
  engageCompletePasswordResetSuccessText: {
    en: "Your new password has sent your email. Please change it on your first login.",
    tr: "Yeni şifreniz email adresinize gönderildi. Lütfen ilk girişinizde şifrenizi değiştirin"
  },
  engageCompletePasswordResetLoginPage: {
    en: "Login",
    tr: "Giriş Yap"
  },
  engageCompletePasswordResetForgotPasswordPage: {
    en: "Re-send Forgot Password Mail",
    tr: "Şifremi Unuttum Mailimi Tekrar Gönder "
  },
  engageConfirmEmailTitleSuccess: {
    en: "Email successfully confirmed",
    tr: "Email başarı ile doğrulandı"
  },
  engageConfirmEmailTitleError: {
    en: "Email confirmation failed!",
    tr: "Email doğrulaması başarısız!"
  },
  engageConfirmEmailTextError: {
    en: "Your confirmation mail might be timed out. Please request another confirmation mail.",
    tr: "Doğrulama mailiniz zaman aşımına uğramış olabilir. Lütfen yeni bir doğrumala maili isteyin"
  },
  engageConfirmEmailContinueHomeButton: {
    en: "Continue",
    tr: "Devam Et"
  },
  engageConfirmEmailRequestNewMail: {
    en: "Send New Confirmation Mail",
    tr: "Yeni Doğrulama Maili Gönder"
  },
  floatingMenuBarHome: {
    en: "Home",
    tr: "Anasayfa"
  },
  floatingMenuBarAccount: {
    en: "Account",
    tr: "Hesap"
  },
  accountPageTab_AccountInfo: {
    en: "Account Info",
    tr: "Hesap Bilgilerin"
  },
  accountPageTab_Team: {
    en: "Team",
    tr: "Ekip"
  },
  accountTabPersonalInfoTitle: {
    en: "Personal Info",
    tr: "Kişisel Bilgiler"
  },
  accountTabEmail: {
    en: "Email",
    tr: "Email"
  },
  accountTabUsername: {
    en: "Username",
    tr: "Kullanıcı Adı"
  },
  accountTabChangePasswordTitle: {
    en: "Change Password",
    tr: "Şifreni Değiştir"
  },
  changePasswordFormCurrentPassword: {
    en: "Current Password",
    tr: "Şuanki Şifre"
  },
  changePasswordFormNewPassword: {
    en: "New Password",
    tr: "Yeni Şifre"
  },
  changePasswordFormNewPasswordConfirm: {
    en: "Password Confirmation",
    tr: "Şifre Doğrulama"
  },
  changePasswordFormChangeButton: {
    en: "Change Password",
    tr: "Şifreyi Değiştir"
  },
  changePasswordFormPasswordsNotMatch: {
    en: "Passwords not match",
    tr: "Şifreler uyuşmuyor"
  },
  changePasswordFormSuccessToast: {
    en: "Password successfully changed",
    tr: "Şifre başarıyla değiştirildi"
  },
  pageInfoTotalRecords: {
    en: "(Total {total} records)",
    tr: "(Toplam {total} kayıt)"
  },
  sideMenuPersonalWorkspace: {
    en: "Your Personal Workspace",
    tr: "Kişisel Çalışma Alanın"
  },
  sideMenuWorkspaceMembers: {
    en: "Members",
    tr: "Üyeler"
  },
  sideMenuWorkspaceTeams: {
    en: "Teams",
    tr: "Ekipler"
  },
  sideMenuWorkspaceCurrentTeam: {
    en: "Current Team",
    tr: "Ekip"
  },
  sideMenuWorkspaceMembersMore: {
    en: "...${number} more",
    tr: "...${number} daha"
  },
  sideMenuWorkspaceInviteMember: {
    en: "Add People",
    tr: "Kişi Ekle"
  },
  sideMenuTeamTopicsMore: {
    en: "${number} more",
    tr: "${number} daha"
  },
  sideMenuTeamTopicsNew: {
    en: "New Topic",
    tr: "Yeni Konu"
  },
  sideMenuTeamMembers: {
    en: "Team members",
    tr: "Ekip üyeleri"
  },
  sideMenuTeamHome: {
    en: "Team Home",
    tr: "Ekip Anasayfası"
  },
  sideMenuTeamSettings: {
    en: "Team settings",
    tr: "Ekip ayarları"
  },
  sideMenuActionsTeams: {
    en: "Actions",
    tr: "Aksiyonlar"
  },
  sideMenuNewTask: {
    en: "New Task",
    tr: "Yeni Görev"
  },
  sideMenuInbox: {
    en: "Inbox",
    tr: "Gelen Kutusu"
  },
  sideMenuActivities: {
    en: "Latest Activities",
    tr: "Son Aktiviteler"
  },
  sideMenuMyAssignments: {
    en: "Assignments",
    tr: "Atananlar"
  },
  sideMenuTeamListNoTeam: {
    en: "Create a team to track your tasks",
    tr: "Görevlerini takip etmek için bir ekip oluştur"
  },
  sideMenuTeamNoTopics: {
    en: "Create a topic to label your tasks",
    tr: "Bir konu oluştur ve görevlerini etiketle"
  },
  sideMenuTeamTopics: {
    en: "Topics",
    tr: "Konular"
  },
  sideMenuTeamTasks: {
    en: "Task",
    tr: "Görevler"
  },
  sideMenuTeamTimeBased: {
    en: "By date",
    tr: "Tarihe göre"
  },
  sideMenuTeamStatusBased: {
    en: "By status",
    tr: "Duruma göre"
  },
  sideMenuTeamThisWeek: {
    en: "This Week",
    tr: "Bu Hafta"
  },
  sideMenuTeamThisMonth: {
    en: "This Month",
    tr: "Bu Ay"
  },
  sideMenuTeamBacklog: {
    en: "Backlog",
    tr: "Geri Plan"
  },
  sideMenuTeamActiveTaskList: {
    en: "Active",
    tr: "Aktif"
  },
  sideMenuTeamTaskListAll: {
    en: "All",
    tr: "Tümü"
  },
  sideMenuTeamArchive: {
    en: "Archive",
    tr: "Arşiv"
  },
  sideMenuCalendar: {
    en: "Calendar",
    tr: "Takvim"
  },
  tabBarMenu: {
    en: "Menu",
    tr: "Menü"
  },
  formRequiredField: {
    en: "Required Field",
    tr: "Zorunlu Alan"
  },
  newTaskModalTitle: {
    en: "New Task",
    tr: "Yeni Görev"
  },
  newTaskModalTeamSelectLabel: {
    en: "Team",
    tr: "Ekip"
  },
  newTaskModalTaskTitle: {
    en: "Title",
    tr: "Başlık"
  },
  newTaskModalTaskDescription: {
    en: "Detail",
    tr: "Detay"
  },
  newTaskModalTaskAssignedDate: {
    en: "Assigned date",
    tr: "Planlanan gün"
  },
  newTaskModalTaskDueDate: {
    en: "Due date",
    tr: "Son tarih"
  },
  newTaskModalTaskTopicLabel: {
    en: "Topic",
    tr: "Konu"
  },
  newTaskModalTaskAssignedToLabel: {
    en: "Assigned to",
    tr: "Atanan kişi"
  },
  newTaskModalTaskTopicNoContentValue: {
    en: "None",
    tr: "Seçilmedi"
  },
  newTaskModalTaskTopicNoContentTooltip: {
    en: "We couldn't find any topics for this team",
    tr: "Bu ekip için oluşturulmuş bir konu bulamadık"
  },
  newTaskModalAssignToYourself: {
    en: "Assign to me",
    tr: "Bana Ata"
  },
  newTaskModalCreate: {
    en: "Create Task",
    tr: "Görev Oluştur"
  },
  newTaskModalCancel: {
    en: "Cancel",
    tr: "Vazgeç"
  },
  teamOptionsMenu: {
    en: "Team Options",
    tr: "Ekip Seçenekleri"
  },
  teamSelectMenu: {
    en: "Teams",
    tr: "Ekipler"
  },
  teamOptionsCurrentTeam: {
    en: "(Current)",
    tr: "(Şuanki)"
  },
  teamOptionsSelectActiveTeamTitle: {
    en: "Change team",
    tr: "Görüntülenen ekibi değiştir"
  },
  newTastCreatedToastText: {
    en: "Task created. \"${taskNo}\"",
    tr: "Görev oluşturuldu. \"${taskNo}\""
  },
  newTastCreatedToastGoToTask: {
    en: "View",
    tr: "Görüntüle"
  },
  newTastCreatedToastCopyUrl: {
    en: "Copy link",
    tr: "Linki Kopyala"
  },
  newTaskCreatedToastCopiedToClipboard: {
    en: "Link copied to clipboard",
    tr: "Link panoya kopyalandı"
  },
  taskDetailPageDescription: {
    en: "Description",
    tr: "Açıklama"
  },
  taskDetailPagePeope: {
    en: "Related People",
    tr: "İlgili Kişiler"
  },
  taskDetailPageCreatedBy: {
    en: "Created By: ",
    tr: "Oluşturan: "
  },
  taskDetailPageAssignedTo: {
    en: "Assignee: ",
    tr: "Atanan: "
  },
  taskDetailPageCreatedAt: {
    en: "Created at",
    tr: "Oluşturma zamanı"
  },
  teamWeeklyScreenBreadcrumbLabel: {
    en: "Weekly Tasks",
    tr: "Haftalık Görevler"
  },
  teamWeeklyScreenWeekNoTitle: {
    en: "Week {weekNo}",
    tr: "{weekNo}. Hafta"
  },
  teamWeeklyScreenWeekStartEndTo: {
    en: "to",
    tr: "-"
  },
  teamWeeklyScreenWeeklyPlanSectionTitle: {
    en: "Weekly Plan",
    tr: "Haftalık Plan"
  },
  teamWeeklyScreenTaskWorkflowStatusSectionTitle: {
    en: "Task Statuses",
    tr: "Görev Durumları"
  },
  taskWeekCardTaskStartedBeforeThisWeekTooltip: {
    en: "Started back at ${date}",
    tr: "${date} tarihinde başladı"
  },
  taskWeekCardTaskDueThisWeekTooltip: {
    en: "Due ${date}",
    tr: "Son tarih ${date}"
  },
  taskWeekCardTaskAssignedToTooltip: {
    en: "Assigned to ${to}",
    tr: "Atanan kişi: ${to}"
  },
  taskWeekCardTaskHasNoAssignedToTooltip: {
    en: "Assign to somebody",
    tr: "Birine ata"
  },
  teamSettingsScreenBreadcrumbLabel: {
    en: "Settings",
    tr: "Ayarlar"
  },
  teamSettingsScreenWorkflowSectionTitle: {
    en: "Workflow",
    tr: "İş Akışı"
  },
  teamSettingsScreenWorkflowSectionDescription: {
    en: "Workflows define the type and order of statuses that tasks go through from start to completion Here you can customize and re-order the workflow statuses.",
    tr: "İş akışları görevlerin başlangıçtan bitişe dek geçeceği durumları belirtir. Buradan iş akış durumlarını kişiselleştirebilirsiniz."
  },
  workflowGroupTitle_BACKLOG: {
    en: "Backlog",
    tr: "Bekleyen"
  },
  workflowGroupTitle_NOT_STARTED: {
    en: "Not Started",
    tr: "Henüz Başlamamış"
  },
  workflowGroupTitle_STARTED: {
    en: "Started",
    tr: "Başlamış"
  },
  workflowGroupTitle_COMPLETED: {
    en: "Completed",
    tr: "Tamamlanan"
  },
  workflowGroupTitle_CANCELLED: {
    en: "Cancelled",
    tr: "Vazgeçildi"
  },
  workflowStatusOrderDownTooltip: {
    en: "Move Down",
    tr: "Aşağı Taşı"
  },
  workflowStatusOrderUpTooltip: {
    en: "Move Up",
    tr: "Yukarı Taşı"
  },
  workflowStatusEditTooltip: {
    en: "Edit",
    tr: "Düzenle"
  },
  workflowStatusDeleteTooltip: {
    en: "Delete",
    tr: "Sil"
  },
  workflowStatusNameEditSave: {
    en: "Save",
    tr: "Kaydet"
  },
  workflowStatusNameEditCancel: {
    en: "Cancel",
    tr: "Vazgeç"
  },
  teamMonthlyScreenBreadcrumbLabel: {
    en: "Monthly Tasks",
    tr: "Aylık Görevler"
  },
  teamMonthlyScreenPeriodStartEndTo: {
    en: "to",
    tr: "-"
  },
  teamMonthlyScreenPeriodPlanSectionTitle: {
    en: "Monthly Plan",
    tr: "Aylık Plan"
  },
  newTopicScreenTitle: {
    en: "New Topic",
    tr: "Yeni Konu"
  },
  topicFormName: {
    en: "Name",
    tr: "Ad"
  },
  topicFormTag: {
    en: "Tag",
    tr: "Etiket"
  },
  topicFormTagExplaination: {
    en: "The topic tag serves to identify the subject of the task without entering task details page.",
    tr: "Konu etiketi, görev detay sayfasına girmeden görevin hangi konuda olduğunu belirtmeye yarar."
  },
  topicFormColor: {
    en: "Color",
    tr: "Renk"
  },
  topicFormSave: {
    en: "Save",
    tr: "Kaydet"
  },
  topicFormDelete: {
    en: "Delete",
    tr: "Sil"
  },
  topicListScreenTitle: {
    en: "Topics",
    tr: "Konular"
  },
  topicListScreenNoContentLabel: {
    en: "No topic found. Create a topic and label your tasks.",
    tr: "Hiçbir konu bulunamadı. Bir konu oluştur ve görevleri konular ile etiketle."
  },
  topicListScreenNoContentNewTopicLabel: {
    en: "Create New Topic",
    tr: "Yeni Konu Oluştur"
  },
  sideMenuTeamThreads: {
    en: "Threads",
    tr: "Tartışmalar"
  },
  sideMenuTeamTaskBoards: {
    en: "Boards",
    tr: "Panolar"
  },
  topicCardEditTooltip: {
    en: "Edit Topic",
    tr: "Konuyu Düzenle"
  },
  topicEditScreenTitle: {
    en: "Edit Topic",
    tr: "Konuyu Düzenle"
  },
  taskDetalPageTaskDescription: {
    en: "No description",
    tr: "Açıklama girilmedi"
  },
  taskDescriptionEdit: {
    en: "Edit",
    tr: "Düzenle"
  },
  taskDescriptionSave: {
    en: "Save",
    tr: "Kaydet"
  },
  taskDescriptionSaving: {
    en: "Saving",
    tr: "Kayıt ediliyor"
  },
  taskDescriptionCancel: {
    en: "Cancel",
    tr: "Vazgeç"
  },
  taskTitleTooShort: {
    en: "Please type more",
    tr: "Daha uzun bir başlık girin"
  },
  taskDetailInfoActionBarAssignTopic: {
    en: "Assign a topic",
    tr: "Bir konu ata"
  },
  taskDetailAssignedDate: {
    en: "Assigned date",
    tr: "Planlanan tarih"
  },
  taskDetailAddAssignedDate: {
    en: "Add Assigned date",
    tr: "Planlanan Tarih Ata"
  },
  taskDetailDueDate: {
    en: "Due date",
    tr: "Son tarih"
  },
  taskDetailAddDueDate: {
    en: "Add Due date",
    tr: "Son Tarih Ata"
  },
  taskDetailCopyLinkTooltip: {
    en: "Copy Link",
    tr: "Linki Kopyala"
  },
  taskDetailAssignToAccount: {
    en: "Unassigned",
    tr: "Kimseye Atanmadı"
  },
  taskDetailAssignedTo: {
    en: "Assigned to",
    tr: "Atandı"
  },
  taskDetailChangeWorkflowStatusTooltip: {
    en: "Change workflow status",
    tr: "İş akış durumunu değiştir"
  },
  changeTaskWorkflowStatusModalTitle: {
    en: "Change Workflow Status",
    tr: "İş Akış Durumunu Değiştir"
  },
  taskDetailChangeTopicTooltip: {
    en: "Change task topic",
    tr: "İşin konusunu değiştir"
  },
  changeTaskTopicModalTitle: {
    en: "Change Task Topic",
    tr: "İşin Konusunu Değiştir"
  },
  changeTaskTopicModalFilterLabel: {
    en: "Filter by topic name and assign new topic",
    tr: "Konu adına göre filtrele ve yeni bir konu ata"
  },
  changeTaskTopicModalFilteredListEmpty: {
    en: "No topic found",
    tr: "Hiçbir konu bulunamadı"
  },
  changeTaskTopicModalRemoveTopic: {
    en: "Remove Task Topic",
    tr: "Görevin Konusunu Kaldır"
  },
  changeTaskDateTitle: {
    en: "Change Task Dates",
    tr: "Görev Tarihlerini Değiştir"
  },
  changeTaskDateTitle_assigned: {
    en: "Change Assigned Date",
    tr: "Planlanan Günü Değiştir"
  },
  changeTaskDateTitle_due: {
    en: "Change Due Date",
    tr: "Son Tarihi Değiştir"
  },
  changeTaskDateDateLabel_assigned: {
    en: "Please select new assigned date",
    tr: "Yeni bir planlanan gün seçin"
  },
  changeTaskDateDateLabel_due: {
    en: "Please select new due date",
    tr: "Yeni bir son gün seçin"
  },
  changeTaskDateDateLabelRemove: {
    en: "Remove Date",
    tr: "Seçimi Kaldır"
  },
  changeTaskDateThisWeek: {
    en: "This Week",
    tr: "Bu Hafta"
  },
  changeTaskDateNextWeek: {
    en: "Next Week",
    tr: "Sonraki Hafta"
  },
  changeTaskDateToday: {
    en: "Today",
    tr: "Bugün"
  },
  changeTaskDateDayAfter: {
    en: "Day After",
    tr: "Sonraki Gün"
  },
  changeTaskDateTomorrow: {
    en: "Tomorrow",
    tr: "Yarın"
  },
  changeTaskDateDueDateIsBeforeAssignedDate: {
    en: "Due date can not be before assigned date",
    tr: "Son Tarih, Planlanan Günden önce olamaz "
  },
  changeTaskDateDateLabelSave: {
    en: "Save",
    tr: "Kaydet"
  },
  changeTaskAssigneeModalTitle: {
    en: "Change Assignee",
    tr: "Atanan Kişiyi Değiştir"
  },
  changeTaskAssigneeModalFilterLabel: {
    en: "Filter team members by name",
    tr: "Ekip üyelerini isme göre filtrele"
  },
  changeTaskAssigneeModalFilteredListEmpty: {
    en: "No one found",
    tr: "Hiç kimse bulunamadı"
  },
  changeTaskAssigneeModalRemove: {
    en: "Remove Assignee",
    tr: "Atamayı Kaldır"
  },
  taskWorkflowActivityInfoLabel_MEMBER_JOIN: {
    en: "",
    tr: ""
  },
  taskWorkflowActivityInfoLabel_MEMBER_LEFT: {
    en: "",
    tr: ""
  },
  taskWorkflowActivityInfoLabel_MEMBER_REMOVED: {
    en: "",
    tr: ""
  },
  taskWorkflowActivityInfoLabel_MEMBER_REQUESTED_ACCESS: {
    en: "",
    tr: ""
  },
  taskWorkflowActivityInfoLabel_CHECKLIST_INITIALIZED: {
    en: "initialized a checklist.",
    tr: "bir kontrol listesi oluşturdu."
  },
  taskWorkflowActivityInfoLabel_CHECKLIST_REMOVED: {
    en: "removed checklist.",
    tr: "Kontrol listesi silindi."
  },
  taskWorkflowActivityInfoLabel_CHECKLIST_TITLE_CHANGED: {
    en: "changed checklist title.",
    tr: "kontrol listesi başlığını değiştirildi."
  },
  taskWorkflowActivityInfoLabel_CHECKLIST_ITEM_CHECKED_STATUS_CHANGED: {
    en: "changed checklist item status.",
    tr: "kontrol listesi maddesinin durumunu değiştirdi."
  },
  taskWorkflowActivityInfoLabel_CHECKLIST_ITEM_LABEL_CHANGED: {
    en: "changed checklist item label.",
    tr: "kontrol listesi maddesinin i̇çeriğini değiştirdi."
  },
  taskWorkflowActivityInfoLabel_CHECKLIST_ITEM_REMOVED: {
    en: "removed checklist item.",
    tr: "kontrol listesi maddesini sildi."
  },
  taskWorkflowActivityInfoLabel_CHECKLIST_ITEM_INITIALIZED: {
    en: "initialized checklist item.",
    tr: "kontrol listesi maddesi oluşturdu."
  },
  taskWorkflowActivityInfoLabel_TASK_INITIALIZED: {
    en: "has created the task.",
    tr: "görev oluşturdu."
  },
  taskWorkflowActivityInfoLabel_TASK_CLOSED: {
    en: "has closed the task.",
    tr: "görevi sonlandırdı."
  },
  taskWorkflowActivityInfoLabel_EDIT_TASK_TITLE: {
    en: "has changed task title.",
    tr: "başlığı değiştirdi."
  },
  taskWorkflowActivityInfoLabel_EDIT_TASK_DESC: {
    en: "has changed task decription.",
    tr: "açıklamayı değiştirdi."
  },
  taskWorkflowActivityInfoLabel_TASK_UPDATE_TOPIC: {
    en: "has changed task topic.",
    tr: "görev konusunu değiştirdi."
  },
  taskWorkflowActivityInfoLabel_TASK_UPDATE_WORKFLOW_STATUS: {
    en: "has changed workflow status.",
    tr: "görev durumunu değiştirdi."
  },
  taskWorkflowActivityInfoLabel_TASK_CHANGE_ASSIGNEE: {
    en: "has changed assignee.",
    tr: "atanan kişiyi değiştirdi."
  },
  taskWorkflowActivityInfoLabel_TASK_CHANGE_ASSIGNED_DATE: {
    en: "has changed assigned date.",
    tr: "planlanan tarihi değiştirdi."
  },
  taskWorkflowActivityInfoLabel_TASK_CHANGE_DUE_DATE: {
    en: "has changed due date.",
    tr: "son tarihi değiştirdi."
  },
  taskWorkflowActivityInfoLabel_RELATION_INITIALIZED: {
    en: "has initialized new task relation.",
    tr: "görev ilişkisi oluşturdu."
  },
  taskWorkflowActivityInfoLabel_RELATION_REMOVED: {
    en: "has removed task relation.",
    tr: "görev ilişkisini kaldırdı."
  },
  taskWorkflowActivityInfoLabel_ATTACHMENT_ADDED: {
    en: "has added an attachment.",
    tr: "bir dosya ekledi."
  },
  taskWorkflowActivityInfoLabel_ATTACHMENT_DELETED: {
    en: "has deleted an attachment.",
    tr: "bir dosya sildi."
  },
  taskWorkflowActivityInfoLabel_TASK_BOARD_ENTRY_INIT: {
    en: "has added task to a board.",
    tr: "görevi bir panoya ekledi."
  },
  taskWorkflowActivityInfoLabel_TASK_BOARD_ENTRY_REMOVED: {
    en: "has removed task from a board.",
    tr: "görevi bir panodan kaldırdı."
  },
  taskWorkflowActivityInfoLabel_TASK_BOARD_ENTRY_ORDER_CHANGE: {
    en: "has changed task order from a board.",
    tr: "görevin bir panodaki sırasını değiştirdi."
  },
  taskWorkflowActivityInfoLabel_TASK_NEW_COMMENT: {
    en: "added new comment.",
    tr: " yeni bir yorum ekledi."
  },
  dateDiffLabelDateInDays: {
    en: "${num} days ago.",
    tr: "${num} gün önce"
  },
  dateDiffLabelDateInHours: {
    en: "${num} hours ago.",
    tr: "${num} saat önce"
  },
  dateDiffLabelDateInMinutes: {
    en: "${num} minutes ago.",
    tr: "${num} dakika önce"
  },
  dateDiffLabelDateJustNow: {
    en: "Just now",
    tr: "Biraz önce"
  },
  taskWorkflowActivityInfoDescriptionShowMore: {
    en: "Show Diff",
    tr: "Farkı Göster"
  },
  taskWorkflowActivityInfoDescriptionShowLess: {
    en: "Show Less",
    tr: "Gizle"
  },
  taskWorkflowActivityInfoAssigneeNoone: {
    en: "No one",
    tr: "Hiç Kimse"
  },
  taskWorkflowActivityInfoAssigneeNoDate: {
    en: "No Date",
    tr: "Tarih Seçilmedi"
  },
  taskListScreenBreadcrumb_active: {
    en: "Active",
    tr: "Aktif"
  },
  taskListScreenBreadcrumb_backlog: {
    en: "Backlog",
    tr: "Geri Plan"
  },
  taskListScreenBreadcrumb_archive: {
    en: "Archive",
    tr: "Arşiv"
  },
  taskListScreenBreadcrumb_all: {
    en: "All Tasks",
    tr: "Tüm Görevler"
  },
  workflowTaskListEmpty: {
    en: "Task list is empty",
    tr: "Görev listesi boş"
  },
  newWorkspaceButtonTooltip: {
    en: "Create a new workspace",
    tr: "Yeni çalışma alanı oluştur"
  },
  newWorkspaceModalTitle: {
    en: "New Workspace",
    tr: "Yeni Çalışma Alanı"
  },
  newWorkspaceFormWorkspaceTitle: {
    en: "Workspace name",
    tr: "Çalışma alanı adı"
  },
  newWorkspaceFormWorkspaceHandleShort: {
    en: "Workspace URL",
    tr: "Çalışma Alanı Linki"
  },
  newWorkspaceFormWorkspaceHandleInfo: {
    en: "${host}/${username}",
    tr: "${host}/${username}"
  },
  newWorkspaceFormWorkspaceDescription: {
    en: "Description (Optional)",
    tr: "Açıklama (Zorunlu Değil)"
  },
  newWorkspaceFormCreate: {
    en: "Create",
    tr: "Oluştur"
  },
  newWorkspaceFormCancel: {
    en: "Cancel",
    tr: "Vazgeç"
  },
  newWorkspaceFormPersonalTitle: {
    en: "Personal Workspace",
    tr: "Kişisel Çalışma Alanı"
  },
  newWorkspaceFormCollaborativeTitle: {
    en: "Collaborative Workspace",
    tr: "Kolektif Çalışma Alanı"
  },
  newWorkspaceFormSearchTitle: {
    en: "Search Existing",
    tr: "Var olanları ara"
  },
  newWorkspaceFormPersonalText: {
    en: "You can track your tasks create reminders etc.",
    tr: "Görevlerinizi takip edebilir anımsatıcılar oluşturabilirsiniz."
  },
  newWorkspaceFormCollaborativeText: {
    en: "You can invite other people to this workspace. You can create teams and assign tasks to people.",
    tr: "Diğer kişileri bu çalışma alanına davet edebilir, ekipler oluşturabilir ve kişilere görevler atayabilirsiniz."
  },
  newWorkspaceFormSearchText: {
    en: "Search and join existing workspace.",
    tr: "Katılmak için var olan bir çalışma alanı arayın."
  },
  newWorkspaceTitlePostFix: {
    en: "${username}'s Workspace",
    tr: "${username}'ın Çalışma Alanı"
  },
  newTeamModalTitle: {
    en: "New Team",
    tr: "Yeni Ekip"
  },
  newTeamFormName: {
    en: "Team name",
    tr: "Ekip adı"
  },
  newTeamFormTag: {
    en: "Tag (short code for easy access. For example Sales -> SLS)",
    tr: "Ekip kısa kodu (Kolay erişim için. Örneğin Satış -> STS)"
  },
  newTeamTaskVisibility: {
    en: "Task visibility",
    tr: "Görev görünülülürlüğü"
  },
  currentWorkspaceHeaderWorkspaceDetail: {
    en: "Workspace detail",
    tr: "Çalışma Alanı detayları"
  },
  taskDetailCreateRelatedTasklist: {
    en: "Link Task",
    tr: "Görev İlişkilendir"
  },
  taskSubtaskList: {
    en: "Sub Tasks",
    tr: "Alt Görevler"
  },
  taskSubtaskListEmpty: {
    en: "No sub tasks found for current task",
    tr: "Bu görev için iliştirilmiş herhangi bir alt görev bulunamadı"
  },
  taskSubtaskListAddNewTaskButton: {
    en: "New Task",
    tr: "Yeni Görev"
  },
  taskSubtaskListAddExistingTaskButton: {
    en: "Existing Task",
    tr: "Var Olan Görev"
  },
  taskSubtaskListAddNewTaskButtonTooltip: {
    en: "Create a new subtask",
    tr: "Alt görev oluştur"
  },
  taskSubtaskListAddExistingTaskButtonTooltip: {
    en: "Add existing task as a new subtask",
    tr: "Var olan görevi ilişkilendir"
  },
  searchTaskModalTitle: {
    en: "Search Task",
    tr: "Görev Ara"
  },
  searchTaskModalPlaceholder: {
    en: "Search task by title",
    tr: "Görev başlığına göre ara"
  },
  searchTaskModalInitialState: {
    en: "To list tasks search with title",
    tr: "Görevleri listelemek için bir görev başlığı ara"
  },
  searchTaskModalEmptyState: {
    en: "No task found",
    tr: "Görev bulunamadı"
  },
  taskRelationUnlink_dialogModalTitle: {
    en: "Remove Relation",
    tr: "İlişiği Kes"
  },
  taskRelationUnlink_dialogModalContent: {
    en: "Are you sure to remove this task relation?",
    tr: "Bu görev ile ilişiği kesmekten emin misiniz?"
  },
  taskRelationUnlink_dialogModalConfirmLabel: {
    en: "Remove",
    tr: "İlişiği Kes"
  },
  dialogModalGenericCloseLabel: {
    en: "Dismiss",
    tr: "Vazgeç"
  },
  taskActivityListTitle: {
    en: "Activities",
    tr: "Aktivite"
  },
  taskRelationListTitle: {
    en: "Relations",
    tr: "İlişkili"
  },
  textEditorBold: {
    en: "Bold",
    tr: "Bold"
  },
  textEditorItalic: {
    en: "Italic",
    tr: "Italic"
  },
  textEditorHeading: {
    en: "Heading",
    tr: "Başlık"
  },
  textEditorQuote: {
    en: "Quote",
    tr: "Alıntı"
  },
  textEditorUl: {
    en: "List",
    tr: "Liste"
  },
  textEditorOl: {
    en: "Ordered List",
    tr: "Sıralı Liste"
  },
  taskRowChangeTaskDates: {
    en: "Change Dates",
    tr: "Tarihleri Değiştir"
  },
  taskRowBoardsTooltip: {
    en: "Boards",
    tr: "Panolar"
  },
  taskDetailRemindersButtonLabel: {
    en: "Reminders",
    tr: "Anımsatıcılar"
  },
  taskReminderListModalTitle: {
    en: "Reminders",
    tr: "Anımsatıcılar"
  },
  taskReminderListModalNoRemindersExist: {
    en: "There isn't any reminders related to this task.",
    tr: "Bu görev ile ilişkili herhangi bir anımsatıcı bulunamadı."
  },
  taskReminderListModalNewReminder: {
    en: "New Reminder",
    tr: "Yeni Anımsatıcı"
  },
  taskNewReminderModalTitle: {
    en: "New Reminder",
    tr: "Yeni Anımsatıcı"
  },
  taskNewReminderModalSpecificDateLabel: {
    en: "Specific date and time",
    tr: "Belirli bir tarih ve saatte"
  },
  taskNewReminderModalRepeat: {
    en: "Repeat",
    tr: "Yenile"
  },
  taskNewReminderModalRepeatEndLabel: {
    en: "Repeat reminder until specific date",
    tr: "Belirli bir tarihe dek yenile"
  },
  taskNewReminderModalRepeatEndDateLabel: {
    en: "End date",
    tr: "Sonlandırma tarihi"
  },
  taskNewReminderModalOnAssignedDateLabel: {
    en: "On assigned date and time",
    tr: "Planlanan gün ve zamanda"
  },
  taskNewReminderModalOnDueDateLabel: {
    en: "On due date and time",
    tr: "Son tarihte"
  },
  taskNewReminderModalReminderRepeatType_NONE: {
    en: "None",
    tr: "Hiçbir Zaman"
  },
  taskNewReminderModalReminderRepeatType_HOURLY: {
    en: "Hourly",
    tr: "Saatlik"
  },
  taskNewReminderModalReminderRepeatType_DAILY: {
    en: "Daily",
    tr: "Günlük"
  },
  taskNewReminderModalReminderRepeatType_WEEKLY: {
    en: "Weekly",
    tr: "Haftalık"
  },
  taskNewReminderModalReminderRepeatType_BIWEEKLY: {
    en: "Biweekly",
    tr: "2 Haftada Bir"
  },
  taskNewReminderModalReminderRepeatType_MONTHLY: {
    en: "Monthly",
    tr: "Aylık"
  },
  taskNewReminderModalReminderRepeatType_EVERY_3_MONTHS: {
    en: "Every 3 Months",
    tr: "Her 3 Ayda Bir"
  },
  taskNewReminderModalReminderRepeatType_EVERY_6_MONTHS: {
    en: "Every 6 Months",
    tr: "Her 6 Ayda Bir"
  },
  taskNewReminderModalReminderRepeatType_YEARLY: {
    en: "Yearly",
    tr: "Yılda Bir"
  },
  taskNewReminderCreateButton: {
    en: "Initialize Reminder",
    tr: "Anımsatıcı Oluştur"
  },
  taskNewReminderCloseButton: {
    en: "Dismiss",
    tr: "Vazgeç"
  },
  taskNewReminderProvideSpecificDate: {
    en: "Please provide a date",
    tr: "Lütfen tarih girin"
  },
  taskReminderType_ASSIGNED_DATE: {
    en: "Assigned Date",
    tr: "Planlanan Tarih"
  },
  taskReminderType_DUE_DATE: {
    en: "Due Date",
    tr: "Son Tarih"
  },
  taskReminderType_SPECIFIC_DATE: {
    en: "Specific Date",
    tr: "Belirli Tarih"
  },
  reminderListItemRepatInfoLabel: {
    en: "Repats ",
    tr: "Yineleme "
  },
  calendarDayMore: {
    en: "more...",
    tr: "daha..."
  },
  newTaskModalUnsetDate: {
    en: "Unset",
    tr: "Kaldır"
  },
  newTaskModalToday: {
    en: "Today",
    tr: "Bugün"
  },
  newTaskModalTomorrow: {
    en: "Tomorrow",
    tr: "Yarın"
  },
  newTaskModalStartOfNextWeek: {
    en: "Next Week",
    tr: "Sonraki Hafta"
  },
  newTaskModalDayAfterAssignedDate: {
    en: "Day after assigned day",
    tr: "Atanan günün ertesi gün"
  },
  workspaceMenuPersonalTitle: {
    en: "My Personal Workspace",
    tr: "Kişisel Çalışma Alanım"
  },
  allTasksPageListTitle: {
    en: "All Tasks",
    tr: "Tüm Görevler"
  },
  datePickerModalQuickActionToday: {
    en: "Today",
    tr: "Bugün"
  },
  datePickerModalTitle: {
    en: "Pick A Date",
    tr: "Bir Tarih Seç"
  },
  datePickerSelectDate: {
    en: "Select Date",
    tr: "Tarih Seçin"
  },
  topicTaskListName: {
    en: "Listing tasks '${topicTag}'",
    tr: "Görevler listeleniyor '${topicTag}'"
  },
  engageWorkspaceInvitationTitle: {
    en: "${fromName} invited you to join ${workspaceName}",
    tr: "${fromName} seni ${workspaceName} çalışma alanına davet etti"
  },
  engageWorkspaceInvitationAcceptButton: {
    en: "Join",
    tr: "Katıl"
  },
  engageWorkspaceInvitationDeclineButton: {
    en: "Decline",
    tr: "Reddet"
  },
  engageWorkspaceInvitationGoBackButton: {
    en: "Home",
    tr: "Anasayfa"
  },
  engageWorkspaceInvitationYoureLoggedInAs: {
    en: "You're logged in as",
    tr: "Şu kullanıcı olarak oturum açtınız"
  },
  engageWorkspaceInvitationLogout: {
    en: "Logout",
    tr: "Çıkış"
  },
  engageWorkspaceInvitationAboutJinear: {
    en: "Jinear helps you track your tasks collaboratively",
    tr: "Jinear, görevlerinizi çalışma arkadaşlarınızla birlikte takip etmenize yardımcı olur."
  },
  engageWorkspaceInvitationLogOutCurrentAccount: {
    en: "You're logged in as <b>${currentAccountEmail}</b>. This invitation is for <b>${invitationToEmail}</b>. Please logout from your current account to accept invitation.",
    tr: "<b>${currentAccountEmail}</b> hesabı ile oturum açtınız. Bu davetiye <b>${invitationToEmail}</b> kişisine gönderildi. Lütfen devam etmek için oturumu kapatın."
  },
  engageWorkspaceInvitationAccepted: {
    en: "You're now part of the <b>${workspaceName}</b> workspace",
    tr: "Artık <b>${workspaceName}</b> çalışma alanının bir parçasısınız"
  },
  engageWorkspaceInvitationAcceptedLoginInfoText: {
    en: "If you did not have an account before we've created one for you. If you wish, you can log in with the single-use login code we will provide you via email. Or you can generate a new password for your account via forgot password flow.",
    tr: "Daha önce bir hesabınız yoksa, sizin için bir tane oluşturduk. İsterseniz, e-posta yoluyla size sağlayacağımız tek kullanımlık giriş kodu ile giriş yapabilirsiniz. Veya şifre sıfırlama akışı aracılığıyla hesabınız için yeni bir şifre oluşturabilirsiniz."
  },
  engageWorkspaceInvitationAcceptedLoginWithEmailCode: {
    en: "Login With Email Code",
    tr: "Giriş Kodu ile Oturum Aç"
  },
  engageWorkspaceInvitationAcceptedLoginWithPassword: {
    en: "Login With Password",
    tr: "Şifre ile Oturum Aç"
  },
  engageWorkspaceInvitationAcceptedResetPassword: {
    en: "Reset Password",
    tr: "Şifre Sıfırla"
  },
  sideMenuFooterLogout: {
    en: "Logout",
    tr: "Çıkış Yap"
  },
  workspaceMemberInviteModalTitle: {
    en: "Invite Someone to Workspace",
    tr: "Çalışma Alanına Birini Davet Et"
  },
  workspaceMemberInvititationFormEmailLabel: {
    en: "Email",
    tr: "Email"
  },
  workspaceMemberInvititationFormEmailText: {
    en: "We will send an invitation mail.",
    tr: "Davet edeceğiniz kişiye bir davetiye linki gönderilecek"
  },
  workspaceMemberInvititationFormForRoleLabel: {
    en: "Workspace Role",
    tr: "Çalışma Alanı Yetkisi"
  },
  workspaceMemberInvititationFormForRole_GUEST: {
    en: "Guest",
    tr: "Misafir"
  },
  workspaceMemberInvititationFormForRole_ADMIN: {
    en: "Admin",
    tr: "Yönetici"
  },
  workspaceMemberInvititationFormForRole_MEMBER: {
    en: "Team Member",
    tr: "Ekip Üyesi"
  },
  workspaceMemberInvititationFormForRoleText_GUEST: {
    en: "Guest role is read only. Members only view tasks can not edit them.",
    tr: "Misafir rolünde kullanıcılar görevleri yanlızca görüntüler, değişiklik yapamazlar."
  },
  workspaceMemberInvititationFormForRoleText_ADMIN: {
    en: "Admins can add new members to workspace, initialize and edit teams, tasks etc.",
    tr: "Yöneticiler çalışma alanına yeni kişiler davet edebilir. Ekipleri ve taskları oluşturabilir ve düzenleyebilirler. "
  },
  workspaceMemberInvititationFormForRoleText_MEMBER: {
    en: "Members can create and edit tasks but can not change workspace settings and members.",
    tr: "Ekip üyeleri görevler oluşturabilir ve düzenleyebilirler ancak çalışma alanı ayarlarını değiştiremez, üye ekleyip çıkartamazlar."
  },
  workspaceMemberInvititationFormInitialTeam: {
    en: "Initial Team",
    tr: "Dahil Olacağı Ekip"
  },
  workspaceMemberInvititationFormInitialTeamText: {
    en: "Member can be added/removed to other teams later.",
    tr: "Üye daha sonra diğer ekiplere de eklenip çıkarılabilir."
  },
  workspaceMemberInvititationFormSubmit: {
    en: "Invite",
    tr: "Davet Et"
  },
  activeInvitationListTitle: {
    en: "Active Invitations",
    tr: "Bekleyen Davetler"
  },
  activeInvitationListEmptyMessage: {
    en: "No active invitations found",
    tr: "Bekleyen davetiye bulunamadı"
  },
  activeInvitationCancel: {
    en: "Cancel",
    tr: "Vazgeç"
  },
  activeWorkspaceMemberListEmptyMessage: {
    en: "No members found",
    tr: "Hiçbir üye bulunamadı"
  },
  activeWorkspaceMemberListTitle: {
    en: "Workspace Members",
    tr: "Çalışma Alanı Üyeleri"
  },
  activeWorkspaceMemberListText: {
    en: "Only accounts below can see workspace content.",
    tr: "Yanlızca aşağıdaki hesaplar çalışma alanı içeriğine erişebilir."
  },
  activeWorkspaceMemberKick: {
    en: "Kick Member",
    tr: "Üyeyi At"
  },
  makeChannelMemberAdmin: {
    en: "Make Admin",
    tr: "Yönetici Yap"
  },
  makeChannelMemberMember: {
    en: "Remove Admin",
    tr: "Yöneticiliği Kaldır"
  },
  workspaceMemberRole_OWNER: {
    en: "Owner",
    tr: "Alan Sahibi"
  },
  workspaceMemberRole_ADMIN: {
    en: "Admin",
    tr: "Yönetici"
  },
  workspaceMemberRole_MEMBER: {
    en: "Member",
    tr: "Ekip Üyesi"
  },
  workspaceMemberRole_GUEST: {
    en: "Guest",
    tr: "Misafir"
  },
  deleteWorkspaceInvitationAreYouSureTitle: {
    en: "Remove Invitation",
    tr: "Daveti Geri Çek"
  },
  deleteWorkspaceInvitationAreYouSureText: {
    en: "Are you sure to remove this workspace invitation?",
    tr: "Daveti geri çekmek istediğinize emin misiniz?"
  },
  deleteWorkspaceInvitationAreYouSureConfirmLabel: {
    en: "Remove",
    tr: "Geri çek"
  },
  workspaceMemberScreenBreadcrumbTitle: {
    en: "Workspace Members",
    tr: "Çalışma Alanı Üyeleri"
  },
  workspaceMemberScreenInviteMember: {
    en: "Add People",
    tr: "Kişi Ekle"
  },
  deleteWorkspaceMemberAreYouSureTitle: {
    en: "Kick Member",
    tr: "Üyeyi Çalışma Alanından At"
  },
  deleteWorkspaceMemberAreYouSureText: {
    en: "Are you sure to kick member from this workspace?",
    tr: "Üyeyi çalışma alanından atmak istediğinize emin misiniz?"
  },
  deleteWorkspaceMemberAreYouSureConfirmLabel: {
    en: "Kick",
    tr: "At"
  },
  logoutAreYouSureTitle: {
    en: "Logout",
    tr: "Çıkış Yap"
  },
  logoutAreYouSureText: {
    en: "Are you sure to logout?",
    tr: "Çıkış yapmak istediğinize emin misiniz?"
  },
  logoutAreYouSureConfirmLabel: {
    en: "Logout",
    tr: "Çık"
  },
  teamMemberScreenBreadcrumbTitle: {
    en: "Team Members",
    tr: "Ekip Üyeleri"
  },
  teamMemberScreenAddMember: {
    en: "Add People",
    tr: "Kişi Ekle"
  },
  teamMemberScreenListTitle: {
    en: "Team Members",
    tr: "Ekip Üyeleri"
  },
  teamMemberScreenListText: {
    en: "Only members below and workspace admins can see team content.",
    tr: "Yanlızca aşağıdaki ekip üyeleri ve çalışma alanı yöneticileri ekip içeriğine erişebilir."
  },
  teamMemberRole_ADMIN: {
    en: "Admin",
    tr: "Yönetici"
  },
  teamMemberRole_MEMBER: {
    en: "Member",
    tr: "Ekip Üyesi"
  },
  teamMemberRole_GUEST: {
    en: "Guest",
    tr: "Misafir"
  },
  deleteTeamMemberAreYouSureTitle: {
    en: "Kick Member",
    tr: "Üyeyi Ekipten At"
  },
  deleteTeamMemberAreYouSureText: {
    en: "Are you sure to kick member from this team?",
    tr: "Üyeyi ekipten atmak istediğinize emin misiniz?"
  },
  deleteTeamMemberAreYouSureConfirmLabel: {
    en: "Kick",
    tr: "At"
  },
  addMemberToTeamModalTitle: {
    en: "Add Someone To Team",
    tr: "Ekibe Birini Ekle"
  },
  addMemberToTeamFormAccountSelectLabel: {
    en: "Select someone from workspace",
    tr: "Eklenecek kişi"
  },
  addMemberToTeamFormAccountSelectNoWorkspaceMembers: {
    en: "There is no suitable person to be added in the workspace.",
    tr: "Çalışma alanında eklenebilecek uygun kimse bulunmuyor."
  },
  addMemberToTeamFormAccountSelectInfo: {
    en: "You can select existing workspace members, if you want to add someone to this workspace please visit ",
    tr: "Var olan çalışma alanı üyelerini seçebilirsiniz. Eğer çalışma alanına birini eklemek istiyorsanız lütfen şuradan devam edin "
  },
  addMemberToTeamFormAccountSelectInfoWorkspaceMembers: {
    en: "Workspace Members",
    tr: "Çalışma Alanı Üyeleri"
  },
  addMemberToTeamFormAccountTeamRole: {
    en: "Team Role",
    tr: "Ekipteki Rolü"
  },
  addMemberToTeamFormRole_ADMIN: {
    en: "Team Admin",
    tr: "Ekip Yöneticisi"
  },
  addMemberToTeamFormRole_MEMBER: {
    en: "Team Member",
    tr: "Ekip Üyesi"
  },
  addMemberToTeamFormRole_GUEST: {
    en: "Guest",
    tr: "Ziyaretçi"
  },
  addMemberToTeamFormRoleText_ADMIN: {
    en: "Team admins can add/remove members to team. Can edit team workflows and tasks.",
    tr: "Ekip yöneticileri ekibe kişi ekleyip çıkartabilir. İş akışlarını ve görevleri düzenleyebilir."
  },
  addMemberToTeamFormRoleText_MEMBER: {
    en: "Team members can edit tasks.",
    tr: "Ekip üyeleri görevler üzerinde değişiklik yapabilirler."
  },
  addMemberToTeamFormRoleText_GUEST: {
    en: "Guests can view tasks. They can not edit tasks.",
    tr: "Ziyaretçiler görevleri yanlızca görüntüleyebilirler. "
  },
  addMemberToTeamFormSubmit: {
    en: "Add",
    tr: "Ekle"
  },
  newWorkspaceScreenTitle: {
    en: "Let's create a initial workspace for you",
    tr: "İlk çalışma alanını yaratalım"
  },
  newWorkspaceScreenText: {
    en: "Workspaces can be personal or shared environments. If you're trying to keep your tasks to yourself continue with a personal workspace or if you work with a team and track tasks together continue with collabrative workspace.",
    tr: "Çalışma alanları kişisel veya paylaşılan ortamlar olabilir. Görevlerinizi kendinize saklamaya çalışıyorsanız kişisel bir çalışma alanıyla devam edin veya ekip olarak çalışıyor ve görevleri birlikte takip ediyorsanız, ortak çalışma alanıyla devam edin."
  },
  newWorkspaceScreenSubtext: {
    en: "You can create more workspaces later.",
    tr: "Daha sonra başka çalışma alanları oluşturabilirsiniz."
  },
  communicationPrefrencesTitle: {
    en: "Communication Prefrences",
    tr: "İletişim Tercihleri"
  },
  communicationPrefrencesEmail: {
    en: "Email",
    tr: "Email"
  },
  communicationPrefrencesPushNotifications: {
    en: "Push Notification",
    tr: "Anlık Bildirimler"
  },
  communicationPrefrencesEmailText: {
    en: "Recieve emails like task reminders. (You will continue receiving system mails like login codes, password reset mails etc.)",
    tr: "Görev anımsatıcıları gibi emailler al. (Şifremi unuttum maili veya giriş kodu gibi sistem mailleri almaya devam edeceksin.)"
  },
  communicationPrefrencesPushNotificationsText: {
    en: "Receive push notifications from your devices related to tasks and etc.",
    tr: "Görevlerinizle ilgili cihazlarınızdan anlık bildirimler alın."
  },
  notificationPermissionModalTitle: {
    en: "Enable Notifications",
    tr: "Anlık Bildirimleri Etkinleştir"
  },
  notificationPermissionModalAllowPermissions: {
    en: "Enable",
    tr: "Etkinleştir"
  },
  notificationPermissionModalDismiss: {
    en: "Dismiss",
    tr: "Kapat"
  },
  notificationPermissionModalInfoText: {
    en: "To receive updates from tasks you subscribe enable notifications.",
    tr: "Takip ettiğin görevler hakkında bildirim almak için bildirimleri etkinleştir."
  },
  inboxScreenBreadcrumbLabel: {
    en: "Inbox",
    tr: "Gelen Kutusu"
  },
  inboxScreenHeader: {
    en: "Inbox",
    tr: "Gelen Kutusu"
  },
  notificationEventListName: {
    en: "Your notifications",
    tr: "Bildirimlerin"
  },
  notificationEventListEmpty: {
    en: "You don't have any notifications",
    tr: "Hiçbir bildiriminiz bulunmuyor"
  },
  taskStatusChangeModalRemovesRemindersTooltip: {
    en: "Changing to this status will remove existing reminders",
    tr: "Görev bu statuye çekildiğinde tüm hatırlatıcılar kapatılır"
  },
  taskOverviewModalGoToTask: {
    en: "Open in page",
    tr: "Tam sayfa görüntüle"
  },
  newTaskFormSubtaskOfLabel: {
    en: "This task will be subtask of <b>${subtaskOfLabel}</b>",
    tr: "Bu görev, şu göreve alt görev olarak açılıyor. <b>${subtaskOfLabel}</b>"
  },
  checklistNewItemPlaceholder: {
    en: "Type to add new item",
    tr: "Madde eklemek için başlık yazın"
  },
  checklistNewChecklistTitle: {
    en: "Checklist",
    tr: "Kontrol Listesi"
  },
  checklistDeleteButtonTooltip: {
    en: "Delete checklist",
    tr: "Kontrol Listesini Sil"
  },
  checklistDeleteAreYouSureTitle: {
    en: "Delete checklist",
    tr: "Kontrol Listesini Sil"
  },
  checklistDeleteAreYouSureText: {
    en: "Are you sure to delete checklist? This action can not be undone!",
    tr: "Kontrol Listesini silmek istediğinizden emin misiniz? Bu işlem geri alınamaz."
  },
  checklistDeleteAreYouSureConfirmText: {
    en: "Delete",
    tr: "Sil"
  },
  taskDetailAddChecklistButtonLabel: {
    en: "Add Checklist",
    tr: "Kontrol Listesi Ekle"
  },
  newChecklistDefaultTitle: {
    en: "Checklist - ${no}",
    tr: "Kontrol Listesi - ${no}"
  },
  taskSubscriptionActionButtonSubscribe: {
    en: "Subscribe",
    tr: "Takip Et"
  },
  taskSubscriptionActionButtonUnsubscribe: {
    en: "Subscribed",
    tr: "Takip Ediliyor"
  },
  taskSubscriptionActionButtonSubscribeTooltip: {
    en: "Stay informed about the process regarding the task",
    tr: "Görev üzerindeki gelişmelerden haberdar ol"
  },
  taskSubscriptionActionButtonUnsubscribeTooltip: {
    en: "Unsubscribe",
    tr: "Takibi Bırak"
  },
  taskActivityChecklistStateChecked: {
    en: "Done",
    tr: "Tamamlandı"
  },
  taskActivityChecklistStateUnchecked: {
    en: "Undone",
    tr: "Tamamlanmadı"
  },
  assigneeTaskListName: {
    en: "Listing tasks assigned to '${assigneeName}'",
    tr: "Şu kişiye atanan görevler listeleniyor. '${assigneeName}'"
  },
  assignedToMeTaskListName: {
    en: "Listing tasks assigned to you",
    tr: "Sana atanan görevler listeleniyor"
  },
  taskHasNewUpdates: {
    en: "There has been updates on this task.",
    tr: "Bu görev üzerinde güncellemeler bulunuyor."
  },
  taskHasNewUpdatesRefreshPage: {
    en: "Refresh Page",
    tr: "Sayfayı Yenile"
  },
  taskHasNewUpdatesDismiss: {
    en: "Dismiss",
    tr: "Önemseme"
  },
  newTaskFormWorkspaceAndTeamInfoLabel: {
    en: "You're creating new task under following workspace and team",
    tr: "Görev oluşturduğun çalışma alanı ve ekip"
  },
  newTaskFormWorkspaceAndTeamInfoForPersonalWorkspaceLabel: {
    en: "You're creating new task under following workspace",
    tr: "Görev oluşturduğun çalışma alanı"
  },
  newTaskModalNewTopicButton: {
    en: "New Topic",
    tr: "Yeni Konu"
  },
  newTopicModalTitle: {
    en: "New Topic",
    tr: "Yeni Konu"
  },
  lastActivitiesScreenAllTeamsTitle: {
    en: "All Activities",
    tr: "Tüm Aktiviteler"
  },
  lastActivitiesScreenAllTeamsText: {
    en: "Listing activities from <b>all teams</b>",
    tr: "<b>Tüm ekiplerdeki</b> son aktiviteler listeleniyor"
  },
  lastActivitiesScreenPersonalWorkspaceText: {
    en: "Listing latest activities from <b>all teams</b>",
    tr: "<b>Tüm ekiplerdeki</b> son aktiviteler listeleniyor"
  },
  lastActivitiesScreenFilteredTitle: {
    en: "Team Activities",
    tr: "Ekip Aktiviteleri"
  },
  lastActivitiesScreenFilteredText: {
    en: "Listing activities from team <b>${teamName}</b>",
    tr: "<b>${teamName}</b> ekibindeki son aktiviteler listeleniyor"
  },
  lastActivitiesScreenFilterButton: {
    en: "Filter",
    tr: "Filtrele"
  },
  lastActivitiesScreenClearFilterButton: {
    en: "Clear Filter",
    tr: "Filtreyi Kaldır"
  },
  activityListEmpty: {
    en: "There isn't any activity",
    tr: "Hiçbir aktivite bulunamadı"
  },
  teamPickerModalPickATeam: {
    en: "Pick a team",
    tr: "Bir ekip seçin"
  },
  newTaskFormPersonalWorkspaceSelected: {
    en: "Your Personal Workspace",
    tr: "Kişisel Çalışma Alanın"
  },
  workspacePickerModalTitle: {
    en: "Select Workspace",
    tr: "Çalışma Alanı Seç"
  },
  workspacePickerModalPickAWorkspace: {
    en: "Pick a workspace",
    tr: "Bir çalışma alanı seçin"
  },
  workspacePickerModalPersonalWorkspace: {
    en: "Your Personal Workspace",
    tr: "Kişisel Çalışma Alanın"
  },

  inboxHeaderPersonalWorkspaceSubtitle: {
    en: "Listing your notifications",
    tr: "Bildirimlerin listeleniyor"
  },
  inboxHeaderFilteredTeamSubtitle: {
    en: "Listing your notifications from team&nbsp;<b>${teamName}</b>",
    tr: "<b>${teamName}</b>&nbsp;ekibindeki bildirimlerin listeleniyor"
  },
  inboxHeaderAllTeamsSubtitle: {
    en: "Listing your notifications from&nbsp;<b>all teams</b>",
    tr: "<b>Tüm ekiplerdeki</b>&nbsp;bildirimlerin listeleniyor"
  },
  inboxScreenFilterButton: {
    en: "Filter",
    tr: "Filtrele"
  },
  inboxScreenClearFilterButton: {
    en: "Clear Filter",
    tr: "Filtreyi Kaldır"
  },
  assignedToMeScreenBreadcrumbLabel: {
    en: "Assigned to me",
    tr: "Bana Atananlar"
  },
  assignedToMeScreenSubtitleAllTeams: {
    en: "Listing tasks assigned to you from&nbsp;<b>all teams</b>",
    tr: "<b>Tüm ekiplerdeki</b>&nbsp;sana atanan görevler listeleniyor"
  },
  assignedToMeScreenSubtitleTeamFiltered: {
    en: "Listing tasks assigned to you from&nbsp;<b>${teamName}</b>",
    tr: "<b>${teamName}</b>&nbsp;ekibindeki sana atanan görevler listeleniyor"
  },
  lastActivitiesScreenBreadcrumbLabel: {
    en: "Last Activities",
    tr: "Son Aktiviteler"
  },
  workspaceSettingsPageWorkspaceInfoTab: {
    en: "Info",
    tr: "Genel Bilgiler"
  },
  userProfilePersonalInfoTitle: {
    en: "Personal Info",
    tr: "Kişisel Bilgiler"
  },
  sideMenuTaskBoardsNoTaskBoardExists: {
    en: "There isn't any task board.",
    tr: "Hiçbir görev panosu bulunamadı."
  },
  newTaskBoardModalTitle: {
    en: "New Task Board",
    tr: "Yeni Görev Panosu"
  },
  newTaskListFormWorkspaceAndTeamInfoForPersonalWorkspaceLabel: {
    en: "You're creating new task board under following workspace",
    tr: "Görev panosu oluşturduğun çalışma alanı"
  },
  newTaskListFormWorkspaceAndTeamInfoLabel: {
    en: "You're creating new task board under following workspace and team",
    tr: "Görev panosu oluşturduğun çalışma alanı ve ekip"
  },
  newTaskListFormPersonalWorkspaceSelected: {
    en: "Your Personal Workspace",
    tr: "Kişisel Çalışma Alanın"
  },
  newTaskListModalTaskListTitle: {
    en: "Title",
    tr: "Başlık"
  },
  newTaskListModalTaskListDueDate: {
    en: "Add Due date",
    tr: "Son Tarih Ekle"
  },
  newTaskListModalCancel: {
    en: "Cancel",
    tr: "Vazgeç"
  },
  newTaskListModalCreate: {
    en: "Create Task Board",
    tr: "Görev Panosu Oluştur"
  },
  sideMenuTeamTaskListsShowMore: {
    en: "...${number} more board(s)",
    tr: "...${number} pano daha"
  },
  taskBoardListScreenBreadcrumbLabel: {
    en: "Task Board",
    tr: "Görev Panosu"
  },
  taskBoardsListTitle: {
    en: "Boards",
    tr: "Panolar"
  },
  taskBoardsListEmptyLabel: {
    en: "There isn't any task board.",
    tr: "Herhangi bir görev panosu bulunamadı."
  },
  taskBoardEmptyLabel: {
    en: "There isn't any task in this board.",
    tr: "Bu panoda herhangi bir görev bulunamadı."
  },
  taskBoardAddTaskButtonLabel: {
    en: "Add Task",
    tr: "Görev Ekle"
  },
  taskBoardRemainingDaysLabelDateInDays: {
    en: "${num} days remain",
    tr: "${num} gün kaldı"
  },
  taskBoardDeadlineTomorrow: {
    en: "Deadline is tomorrow",
    tr: "Son tarih yarın"
  },
  taskBoardDueDatePast: {
    en: "The deadline expired ${num} day(s) ago",
    tr: "${num} gün önce süresi doldu"
  },
  taskBoardDueDateToday: {
    en: "Due date is today",
    tr: "Son tarih bugün"
  },
  taskBoardAssignDueDate: {
    en: "Assign Due Date",
    tr: "Son Tarih Ata"
  },
  taskBoardChangeTitleModalTitle: {
    en: "Update Title",
    tr: "Başlık Güncelle"
  },
  taskBoardChangeTitleModalInfoText: {
    en: "Please provide an another title for task board.",
    tr: "Görev panosu için yeni bir başlık girin."
  },
  taskBoardItemRemoveElementTooltip: {
    en: "Remove from board",
    tr: "Panodan kaldır"
  },
  taskBoardEntryDeleteAreYouSureTitle: {
    en: "Remove from board",
    tr: "Panodan kaldır"
  },
  taskBoardEntryDeleteAreYouSureText: {
    en: "Are you sure to remove this task from board? You can always add this task to any board you like.",
    tr: "Bu görevi panodan kaldırmak istediğine emin misin? Görevi daha sonra tekrar panoya ekleyebilirsin."
  },
  taskBoardEntryDeleteAreYouSureConfirmLabel: {
    en: "Confirm",
    tr: "Onay"
  },
  basicTextInputModalSubmitLabel: {
    en: "Confirm",
    tr: "Devam"
  },
  basicTextInputModalCancelLabel: {
    en: "Cancel",
    tr: "Vazgeç"
  },
  taskTaskBoardModalTitle: {
    en: "Task Boards",
    tr: "Görev Panoları"
  },
  taskTaskBoardModalEmptyState: {
    en: "No task board found",
    tr: "Hiçbir pano bulunamadı"
  },
  taskTaskBoardModalInitialState: {
    en: "To list task boards search with title",
    tr: "Panoları listelemek için başlığı ile ara"
  },
  taskTaskBoardModalFilterPlaceholder: {
    en: "Filter by board name",
    tr: "Pano adına göre filtrele"
  },
  taskBoardsActionButtonTooltip: {
    en: "Add or remove from task board",
    tr: "Panoya ekle/çıkar"
  },
  taskBoardsActionButtonLabel: {
    en: "Boards",
    tr: "Panolar"
  },
  topicPickerModalTitle: {
    en: "Topics",
    tr: "Konular"
  },
  topicPickerModalFilterPlaceholder: {
    en: "Filter by topic name or tag",
    tr: "Konu adı veya etiketine göre filtrele"
  },
  topicPickerModalEmptyState: {
    en: "No topic found",
    tr: "Hiçbir konu bulunamadı"
  },
  topicPickerModalSelectButton: {
    en: "Select",
    tr: "Seç"
  },
  topicPickerModalCancelButton: {
    en: "Cancel",
    tr: "Vazgeç"
  },
  newTaskFormPickTopicButtonLabel: {
    en: "Assign a topic",
    tr: "Bir konu ata"
  },
  newTaskFormPickBoardButtonLabel: {
    en: "Add to Board",
    tr: "Panoya Ekle"
  },
  teamMemberPickerModalTitle: {
    en: "Team Members",
    tr: "Ekip Üyeleri"
  },
  teamMemberPickerModalFilterPlaceholder: {
    en: "Filter by username",
    tr: "Kullanıcı adına göre filtrele"
  },
  teamMemberPickerModalEmptyState: {
    en: "No team member found",
    tr: "Hiçbir ekip üyesi bulunamadı"
  },
  teamMemberPickerModalSelectButton: {
    en: "Select",
    tr: "Seç"
  },
  teamMemberPickerModalCancelButton: {
    en: "Cancel",
    tr: "Vazgeç"
  },
  newTaskFormPickAssigneeButtonLabel: {
    en: "Assign to someone",
    tr: "Birine ata"
  },
  newTaskFormPickAssignedDateButtonLabel: {
    en: "Pick assigned date",
    tr: "Planlanan tarih seç"
  },
  newTaskFormPickDueDateButtonLabel: {
    en: "Pick due date",
    tr: "Son tarih seç"
  },
  taskFilterTopicFilterButtonEmpty: {
    en: "Topic",
    tr: "Konu"
  },
  taskFilterTopicFilterButtonSelected: {
    en: "${count} Topic(s)",
    tr: "${count} Konu"
  },
  taskFilterOwnerFilterButtonEmpty: {
    en: "Owner",
    tr: "Bildiren"
  },
  taskFilterOwnerFilterButtonSingleSelection: {
    en: "Owner: ",
    tr: "Bildiren: "
  },
  taskFilterOwnerFilterButtonSelected: {
    en: "${count} Owners",
    tr: "${count} Bildiren Kişi"
  },
  taskFilterAssigneeFilterButtonEmpty: {
    en: "Assignee",
    tr: "Atanan"
  },
  taskFilterAssigneeFilterButtonSingleSelection: {
    en: "Assignee: ",
    tr: "Atanan: "
  },
  taskFilterAssigneeFilterButtonSelected: {
    en: "${count} Assignees",
    tr: "${count} Atanan"
  },
  teamWorkflowStatusPickerModalTitle: {
    en: "Pick Workflow Status",
    tr: "İş Akış Durumu Seç"
  },
  teamWorkflowStatusPickerModalFilterPlaceholder: {
    en: "Filter by name",
    tr: "Ada göre filtrele"
  },
  teamWorkflowStatusPickerModalEmptyState: {
    en: "No team workflow status found",
    tr: "Hiçbir iş akış durumu bulunamadı"
  },
  teamWorkflowStatusPickerModalSelectButton: {
    en: "Select",
    tr: "Seç"
  },
  teamWorkflowStatusPickerModalCancelButton: {
    en: "Cancel",
    tr: "Vazgeç"
  },
  taskFilterWorkflowStatusFilterButtonEmpty: {
    en: "Status",
    tr: "Durum"
  },
  taskFilterWorkflowStatusFilterButtonSelected: {
    en: "${count} statuses",
    tr: "${count} durum"
  },
  taskFilterFromDateFilterButtonEmpty: {
    en: "After",
    tr: "Sonra"
  },
  taskFilterToDateFilterButtonEmpty: {
    en: "Before",
    tr: "Önce"
  },
  taskListTitleAndViewTypeListTooltip: {
    en: "List View",
    tr: "Liste Görünümü"
  },
  taskListTitleAndViewTypeListLabel: {
    en: "List",
    tr: "Liste"
  },
  taskListTitleAndViewTypeStatusColumnsTooltip: {
    en: "Status Columns View",
    tr: "Durum Görünümü"
  },
  taskListTitleAndViewTypeStatusColumnsLabel: {
    en: "Status Columns",
    tr: "Durum"
  },
  activeTasksPageTitle: {
    en: "Active Tasks",
    tr: "Aktif Görevler"
  },
  archivedTasksPageTitle: {
    en: "Archived Tasks",
    tr: "Arşivlenmiş Görevler"
  },
  backlogTasksPageTitle: {
    en: "Backlogged Tasks",
    tr: "Geri Plandaki Görevler"
  },
  quickFilterBarThisWeek: {
    en: "This Week",
    tr: "Bu Hafta"
  },
  quickFilterBarThisMonth: {
    en: "This Month",
    tr: "Bu Ay"
  },
  quickFilterBarActive: {
    en: "Active",
    tr: "Aktif"
  },
  quickFilterBarUndone: {
    en: "Undone",
    tr: "Tamamlanmamış"
  },
  quickFilterBarArchived: {
    en: "Archive",
    tr: "Arşiv"
  },
  quickFilterBarBacklog: {
    en: "Backlog",
    tr: "Geri Plan"
  },
  quickFilterBarClearAll: {
    en: "Clear",
    tr: "Temizle"
  },
  sideMenuTeamsTitle: {
    en: "Your Teams",
    tr: "Ekiplerin"
  },
  sideMenuArchivedTeamsTitle: {
    en: "Archived Teams (${n})",
    tr: "Arşivlenmiş Ekipler (${n})"
  },
  sideMenuTeamActionButtonLabelTasks: {
    en: "Tasks",
    tr: "Görevler"
  },
  sideMenuTeamActionButtonLabelFeed: {
    en: "Feed",
    tr: "Akış"
  },
  sideMenuTeamActionButtonLabelFiles: {
    en: "Files",
    tr: "Dosyalar"
  },
  sideMenuTeamActionButtonLabelBoards: {
    en: "Boards",
    tr: "Panolar"
  },
  sideMenuTeamActionButtonLabelBoardsNew: {
    en: "New Board",
    tr: "Yeni Pano"
  },
  sideMenuTeamActionButtonLabelTopics: {
    en: "Topics",
    tr: "Konular"
  },
  sideMenuTeamTopicListsShowMore: {
    en: "...${number} more topic(s)",
    tr: "...${number} konu daha"
  },
  tasksScreenBreadcrumbLabel: {
    en: "Tasks",
    tr: "Görevler"
  },
  boardPickerModalTitle: {
    en: "Boards",
    tr: "Panolar"
  },
  boardPickerModalFilterPlaceholder: {
    en: "Filter by board name",
    tr: "Pano adına göre filtrele"
  },
  boardPickerModalEmptyState: {
    en: "No task board found",
    tr: "Hiçbir pano bulunamadı"
  },
  homescreenActionBarPricing: {
    en: "Pricing",
    tr: "Fiyatlandırma"
  },
  homescreenActionBarTerms: {
    en: "Terms & Conditions",
    tr: "Şartlar ve Koşullar"
  },
  homescreenHeroTitleLine1: {
    en: "Calendar, Task Manager & Messaging",
    tr: "Takımlar ve Kişisel kullanım için"
  },
  homescreenHeroTitleLine2: {
    en: "for Individuals and Teams",
    tr: "Takvim, Görev Yönetimi ve Mesajlaşma"
  },
  homescreenHeroText: {
    en: "Track your workflow without all the hustle. Organize your calendar, create reminders for your tasks, manage projects collaboratively. Message with your team mates.",
    tr: "İş akışınızı takip edin. Takviminizi düzenleyin, görevleriniz için hatırlatıcılar oluşturun, projeleri işbirliğiyle yönetin. Takım arkadaşlarınızla mesajlaşın."
  },
  homescreenGoToApp: {
    en: "Go to App",
    tr: "Uygulamaya Git"
  },
  homescreenLogin: {
    en: "Login",
    tr: "Giriş Yap"
  },
  homescreenRegister: {
    en: "Create an account",
    tr: "Hesap Oluştur"
  },
  homescreenCardTitle_Calendar: {
    en: "Calendar and tasks. No bullshit",
    tr: "Görevler ve takvim"
  },
  homescreenCardText_Calendar: {
    en: "Stop mocking around. You don't need fancy graphs and charts, you need to get shit done. Here's simple calendar and your tasks.",
    tr: "Süslü grafikler ve tablolara ihtiyacın yok, işlerin hallolmasına ihtiyacın var. İşte sade bir takvim ve görevlerin."
  },
  homescreenCardTitle_NewTask: {
    en: "What needs to be done?",
    tr: "Hangi işin yapılması gerek?"
  },
  homescreenCardText_NewTask: {
    en: "You don't need to fill something like tax return form to plan your next meeting. With little patience you will be able to teach your grandma to plan next sprint.",
    tr: "Bir sonraki haftanı planlamak için vergi iade formu gibi bir şey doldurmana gerek yok. Biraz sabırla babaannene bile sonraki sprinti planlatabilirsin."
  },
  homescreenCardTitle_Reminder: {
    en: "Remember the milk!",
    tr: "Unutma"
  },
  homescreenCardText_Reminder: {
    en: "You keep forgetting tasks? You want us to remind you what to do? Say no more you piece of irresponsible shit. We'll spam you with email and notifications.",
    tr: "Görevleri sürekli unutuyor musun? Sana ne yapman gerektiğini hatırlatmamızı mı istiyorsun? Tamamdır. Seni e-posta ve bildirim yağmuruna tutacağız."
  },
  homescreenCardTitle_CollabWorkspace: {
    en: "Team up",
    tr: "İşler Birlikte Kolay"
  },
  homescreenCardText_CollabWorkspace: {
    en: "Create a collaborative workspace. Close tasks with your team.",
    tr: "Kolektif bir çalışma alanı oluştur. Görevleri takımın ile birlikte tamamla."
  },
  homescreenCardTitle_Boards: {
    en: "Set goals with Boards!",
    tr: "Panolar yardımıyla hedefler koy!"
  },
  homescreenCardText_Boards: {
    en: "Focus your team on what work should be done next with deadline.",
    tr: "Takımın sonraki aşamada neye odaklanması gerektiğini son tarih atayarak belirle."
  },
  homescreenCardTitle_Task: {
    en: "Don't need fancy words",
    tr: "Laf salatasına gerek yok"
  },
  homescreenCardText_Task: {
    en: "Create and control your tasks. Link them together, assign somebody, track and subscribe to task activity or add a checklist. Simple.",
    tr: "Görevler oluşturun ve kontrol edin. Görevleri birbirine bağlayın, birine atayın, aktiviteleri takip edin veya bir checklist ekleyin. Gayet basit."
  },
  taskDetailMediaTitle: {
    en: "Attachments",
    tr: "Ekler"
  },
  taskDetailMediaAddMedia: {
    en: "Add attachment",
    tr: "Dosya ekle"
  },
  taskDetailMediaUploading: {
    en: "Uploading...",
    tr: "Yükleniyor..."
  },
  taskDetailMediaDeleteMedia: {
    en: "Delete",
    tr: "Sil"
  },
  taskDetailMediaDeleteMediaAreYouSureTitle: {
    en: "Delete Attachment",
    tr: "Dosyayı Sil"
  },
  taskDetailMediaDeleteMediaAreYouSureText: {
    en: "Are you sure to delete attachment? This action can not be undone!",
    tr: "Dosyayı silmek istediğinizden emin misiniz? Bu işlem geri alınamaz!"
  },
  taskDetailMediaDeleteMediaAreYouSureConfirmLabel: {
    en: "Delete",
    tr: "Sil"
  },
  taskDetailMediaListEmpty: {
    en: "There isn't any attachments.",
    tr: "Herhangi bir ek bulunmuyor."
  },
  apiFileTooLargeError: {
    en: "File is too large. Maximum file size is 32Mb",
    tr: "Dosya maksimum boyutun üzerinde. Maksimum dosya boyutu 32Mb"
  },
  teamFilesScreenBreadcrumbTitle: {
    en: "Team Files",
    tr: "Ekip Dosyaları"
  },
  teamFilesListTitle: {
    en: "Files",
    tr: "Dosyalar"
  },
  teamFilesListEmptyLabel: {
    en: "There isn't any files.",
    tr: "Herhangi bir dosya bulunamadı."
  },
  pricingPageStickerTitle: {
    en: "IT'S FREE",
    tr: "TAMAMEN ÜCRETSİZ"
  },
  pricingPageStickerText: {
    en: "All of the features are free during our beta. Than it's 49.99$ for collabrative workspaces with unlimited everything. <br><br>  Personal spaces stay free for ever.",
    tr: "Tüm özellikler beta sürecinde ücretsiz. Sonrasında  kolektif çalısma alanları için tüm özellikler sınırlama olmadan aylık 49.99$. <br><br> Kisisel çalısma alanları sonsuza kadar ücretsiz."
  },
  pricingPageGoBack: {
    en: "Return to home",
    tr: "Anasayfaya dön"
  },
  notificationPermission_denied: {
    en: "Denied",
    tr: "Reddedildi"
  },
  notificationPermission_granted: {
    en: "Granted",
    tr: "İzin verildi"
  },
  notificationPermission_default: {
    en: "Not decided",
    tr: "Karar verilmedi"
  },
  notificationPermission_not_supported: {
    en: "Not supported",
    tr: "Desteklenmiyor"
  },
  workspaceInfoTabWorkspaceType_Personal: {
    en: "Personal Workspace",
    tr: "Kişisel Çalışma Alanı"
  },
  workspaceInfoTabWorkspaceType_Personal_detail: {
    en: "Your personal use only. You cannot invite others.",
    tr: "Size özel, diğer kullanıcılar davet edilemez."
  },
  workspaceInfoTabWorkspaceType_Collaborative_BASIC: {
    en: "Basic Collaborative Workspace",
    tr: "Basit Ortak Çalışma Alanı"
  },
  workspaceInfoTabWorkspaceType_Collaborative_BASIC_detail: {
    en: "Work with up to 3 people. File sharing not allowed.",
    tr: "3 Kişiye kadar birlikte çalışılabilir. Dosya paylaşımı yapılamaz."
  },
  workspaceInfoTabWorkspaceType_Collaborative_PRO: {
    en: "Professional Collaborative Workspace",
    tr: "Profesyonel Ortak Çalışma Alanı"
  },
  workspaceInfoTabWorkspaceType_Collaborative_PRO_detail: {
    en: "Invite as many people you want. Share your files.",
    tr: "İstediğiniz kadar kişiyi çalışma alanına ekleyin. Dosya paylaşımı yapılabilir."
  },
  workspaceTierUpgradeButton: {
    en: "Upgrade Your Plan",
    tr: "Planı Yükselt"
  },
  upgradeWorkspaceTierModalTitle: {
    en: "Upgrade Your Plan",
    tr: "Planı Yükselt"
  },
  upgradeWorkspaceTierModalText: {
    en: "Upgrade workspace to add <span class='workspace-tier-upgrade-highlight'>unlimited users</span> to to your workspace and <span class='workspace-tier-upgrade-highlight'>share files</span> with <span class='workspace-tier-upgrade-highlight'>unlimited file storage</span>.  First-in-line <span class='workspace-tier-upgrade-highlight'>7/24 priority support</span>. <span class='workspace-tier-upgrade-highlight'>1:1 onboarding</span>  tour with our team.",
    tr: "Çalışma alanı planını yükseltin, çalışma alanına <span class='workspace-tier-upgrade-highlight'>sınırsız sayıda kullanıcı</span> ekleyin, <span class='workspace-tier-upgrade-highlight'>sınırsız sayıda dosya paylaşımı</span> yapın. <span class='workspace-tier-upgrade-highlight'>7/24 öncelikli destek</span> alın. Onboarding sürecinizi <span class='workspace-tier-upgrade-highlight'>1:1 destek</span> ile yönetelim."
  },
  upgradeWorkspaceTierModalAppliesToWorkspaceText: {
    en: "This applies only the workspace above. Features specified will be applied to this workspace and teams under it.",
    tr: "Bu işlem yukarıdaki çalışma alanı için geçerlidir. Çalışma alanı ve altındaki takımlarda belirtilen özellikler aktif olacaktır."
  },
  upgradeWorkspaceTierModalAutoRenewsText: {
    en: "Auto renews. Cancel anytime.",
    tr: "Otomatik yenilenir. İstediğiniz zaman iptal edebilirsiniz."
  },
  upgradeWorkspaceTierButtonMonthly: {
    en: "${price} per month",
    tr: "Aylık ${price}"
  },
  upgradeWorkspaceTierButtonYearly: {
    en: "${price} per year (2 Month Free)",
    tr: "Yıllık ${price} (2 Ay ücretsiz)"
  },
  upgradeWorkspaceTierSuccess: {
    en: "We successfully received your purchase. Your workspace will be updated in a moment.",
    tr: "Satın alma başarılı. Çalışma alanın kısa süre içerisinde güncellenecek."
  },
  subscriptionReceiptUrlButtonLabel: {
    en: "Receipt",
    tr: "Fatura"
  },
  subscriptionNextBillingDate: {
    en: "Next billing date is ${date}",
    tr: "Bir sonraki fatura tarihi ${date}"
  },
  subscriptionUpdate: {
    en: "Update payment method",
    tr: "Ödeme yöntemini değiştir"
  },
  subscriptionCancel: {
    en: "Cancel subscription",
    tr: "Aboneliği iptal et"
  },
  billingTitle: {
    en: "Billing",
    tr: "Faturalandırma"
  },
  subscriptionCancelsAfter: {
    en: "Subscription canceled. You can use your plan until ${date}",
    tr: "Abonelik sonlandırıldı. Planınızı ${date} tarihine kadar kullanabilirsiniz."
  },
  subscriptionPastPayments: {
    en: "Payments",
    tr: "Geçmiş Ödemeler"
  },
  termsPageGoBack: {
    en: "Return to home",
    tr: "Anasayfaya dön"
  },
  newChangesExistsToastTitle: {
    en: "New Activities Exists",
    tr: "Yeni Aktiviteler Bulunuyor"
  },
  newChangesExistsToastBody: {
    en: "Go to activities page to see latest activities on workspace.",
    tr: "Çalışma alanındaki son aktiviteleri görmek için son aktiviteler ekranına gidin."
  },
  foregroundNotificationDefaultButtonLabel: {
    en: "Continue",
    tr: "Devam"
  },
  foregroundNotificationDefaultCloseButtonLabel: {
    en: "Close",
    tr: "Kapat"
  },
  taskTitleChangeModalTitle: {
    en: "Update Title",
    tr: "Başlığı Güncelle"
  },
  taskTitleChangeModalInfoText: {
    en: "Please provide an another title for task.",
    tr: "Görev için yeni bir başlık girin."
  },
  pricePageBasicPlan: {
    en: "Free",
    tr: "Ücretsiz"
  },
  pricesPageBasicFeature_UnlimitedTasks: {
    en: "Create unlimited tasks, labels, checklists, boards",
    tr: "Sınırsız görev, etiket, kontrol listesi, pano oluşturun"
  },
  pricesPageBasicFeature_Reminders: {
    en: "Reminders! Set up reminders about your tasks.",
    tr: "Görevleriniz için anımsatıcılar oluşturun"
  },
  pricesPageBasicFeature_collaborative: {
    en: "Create collaborative workspaces and work with up to 3 people.",
    tr: "Kolektif çalışma alanları yaratarak 3 Kişiye kadar birlikte çalışın."
  },
  pricesPageProFeature_collaborative: {
    en: "Add as many people as you need to your collaborative workspace.",
    tr: "Kolektif çalışma alanına istediğiniz kadar kişi ekleyin."
  },
  pricesPageProFeature_fixedPrices: {
    en: "Fixed prices, no absurd per user charges.",
    tr: "Sabitlenmiş fiyatlandırma, kişi başı absürt ücretlendirmeler yok."
  },
  pricesPageProFeature_team_task_visibility: {
    en: "Increased task visibility settings.",
    tr: "Arttırılmış görev görüntüleme izinleri."
  },
  pricesPageProFeature_team_task_visibility_description: {
    en: "You can set task visiblity to only task owners & assignees on teams that handles sensitive jobs.",
    tr: "Hassas işleri yöneten takımlarda, görev görünürlüğünü sadece görev sahiplerinin ve atanmış kişilerin görebilmesini sağlayın."
  },
  pricesPageProFeature_file: {
    en: "Attach files to your tasks.",
    tr: "Görevlerinize dosya ekleyin."
  },
  pricesPageProFeature_unlimitedFileStorage: {
    en: "100 GB file storage.",
    tr: "100 GB dosya saklama alanı."
  },
  pricesPageProFeature_UnlimitedSupport: {
    en: "7/24 unlimited support.",
    tr: "7/24 sınırsız destek alın."
  },
  pricesPageProFeature_1to1Onboarding: {
    en: "1:1 onboarding tour.",
    tr: "Uygulamaya dahil olma sürecinizde 1'e 1 görüşme ile destek."
  },
  pricesPageProFeature_additionalToBasicPlanText: {
    en: "Everything in free plan...",
    tr: "Basic plandaki herşeye ek olarak..."
  },
  pricesPageSelfHostFeature_additionalToProPlanText: {
    en: "Everything in pro plan...",
    tr: "Pro plandaki herşeye ek olarak..."
  },
  pricesPageProFeature_subscriptionsAppliesOnlySingleWorkspaceText: {
    en: "* Each workspace has it's own plan. Upgrading a workspace to pro plan only affects itself.",
    tr: "* Her bir çalışma alanı kendi planına sahiptir. Bir çalışma alanını 'Pro' plana yükseltmek yanlızca o çalışma alanını etkiler. Diğer çalışma alanlarına belirtilen özellikler gelmez."
  },
  pricesPageProFeature_singleFileSizeLimit: {
    en: "* While there is 100 GB limit to file storage, currently, the size of each single file can be a maximum of 32MB.",
    tr: "* Toplam yüklenebilecek dosya limiti 100 GB olmasina rağmen şu anda her bir tekil dosyanın boyutu maksimum 32MB olabilir."
  },
  pricesPageSelfHostFeature_PayOnce: {
    en: "Pay once use forever ",
    tr: "Bir kere ödeyin sonsuza dek kullanın"
  },
  pricesPageSelfHostFeature_Own: {
    en: "Self host your Jinear instance.",
    tr: "Jinear'ı kendi sunucunuza kurun."
  },
  pricesPageSelfHostFeature_Privacy: {
    en: "Best for regulated fields.",
    tr: "Regülatif kısıtlamalara takılmayın."
  },
  pricesPageSelfHostFeature_EasyInstall: {
    en: "Easy install. Single docker compose file all you need.",
    tr: "Kolay kurulum. Tek bir docker compose dosyasını çalıştırmanız yeterli."
  },
  pricesPageSelfHostFeature_RequiresExpertise: {
    en: "* You manage your own server. Requires at least a little technical knowledge.",
    tr: "* Kendi sunucunuzu yönetirsiniz. En azından biraz teknik bilgi gerektirir."
  },
  pricesPageSelfHostFeature_Updates: {
    en: "You get one year free upgrades.",
    tr: "Bir yıl boyunca güncellemeler ücretsizdir."
  },
  pricesPageSelfHostFeature_Price: {
    en: "${price} Once",
    tr: "${price} Bir kez"
  },
  pricesPageSelfHostFeature_RenewLicencePrice: {
    en: "${price} Renewing existing licence",
    tr: "${price} Lisans yenileme"
  },
  pricesPageSelfHostFeature_RenewLicence: {
    en: "* Renewing licence is to get new updates after 1 year. If you're happy with your latest updates you can keep using your existing version forever without renewing your licence.",
    tr: "* Lisans yenileme bir yıldan sonra yeni güncellemeleri almaya devam etmek için gerekir. Lisans yenilemeden var olan sürümünüzü sonsuza dek kullanabilirsiniz."
  },
  pricingPageTitle: {
    en: "Pricing & Plans",
    tr: "Planlar ve Fiyatlandırma"
  },
  calendarViewTypeSegment_Month: {
    en: "Month",
    tr: "Ay"
  },
  calendarViewTypeSegment_Day: {
    en: "Day",
    tr: "Gün"
  },
  calendarViewTypeSegment_2Day: {
    en: "2 Day",
    tr: "2 Gün"
  },
  calendarViewTypeSegment_Week: {
    en: "Week",
    tr: "Hafta"
  },
  calendarWeekViewAllDayLabel: {
    en: "All Day",
    tr: "Tüm Gün"
  },
  taskCommentsTitle: {
    en: "Comments",
    tr: "Yorumlar"
  },
  taskCommentsInputPlaceholder: {
    en: "Leave a comment",
    tr: "Bir yorum bırakın"
  },
  taskCommentsCommentSubmit: {
    en: "Submit",
    tr: "Gönder"
  },
  taskCommentsCommentCancel: {
    en: "Cancel",
    tr: "Vazgeç"
  },
  taskCommentsCommentHeaderText: {
    en: "added a comment",
    tr: "bir yorum ekledi"
  },
  taskCommentDelete: {
    en: "Delete",
    tr: "Sil"
  },
  deleteCommentAreYouSureTitle: {
    en: "Delete Comment",
    tr: "Yorumu Sil"
  },
  deleteCommentAreYouSureText: {
    en: "Are you sure to delete this comment?",
    tr: "Bu yorumu silmek istediğinize emin misiniz?"
  },
  deleteCommentAreYouSureDeleteButton: {
    en: "Delete",
    tr: "Sil"
  },
  commentThisCommentDeleted: {
    en: "Comment deleted by user.",
    tr: "Kullanıcı tarafından bu yorum silinmiş."
  },
  commentLastUpdateDate: {
    en: "Last update ",
    tr: "Son güncelleme "
  },
  taskCommentReply: {
    en: "Reply",
    tr: "Alıntıla"
  },
  taskCommentCreateDate: {
    en: "Initialized at ",
    tr: "Oluşturma "
  },
  taskCommentUpdateDate: {
    en: "Updated at ",
    tr: "Güncelleme "
  },
  taskCommentQuote: {
    en: "Quote",
    tr: "Alıntı"
  },
  taskCommentListEmpty: {
    en: "There isn't any comments about this task.",
    tr: "İlk yorum yapan siz olun"
  },
  datePickerButtonPickFutureDate: {
    en: "Please pick future date",
    tr: "Gelecek tarih seçin"
  },
  datePickerButtonPickPastDate: {
    en: "Please pick past date",
    tr: "Daha geri bir tarih seçin"
  },
  mainFeaturesMenuLabelTasks: {
    en: "Tasks",
    tr: "Görevler"
  },
  mainFeaturesMenuLabelCalendar: {
    en: "Calendar",
    tr: "Takvim"
  },
  mainFeaturesMenuLabelNotifications: {
    en: "Notifications",
    tr: "Bildirimler"
  },
  mainFeaturesMenuLabelLastActivities: {
    en: "Updates",
    tr: "Son Aktiviteler"
  },
  mainFeaturesMenuLabelAssignedToMe: {
    en: "Assignments",
    tr: "Atananlar"
  },
  mainFeaturesMenuLabelConversations: {
    en: "Messages",
    tr: "Sohbetler"
  },
  mainFeaturesMenuLabelMore: {
    en: "More",
    tr: "Daha Fazla"
  },
  tasksLayoutSideMenuCollapsedLabel: {
    en: "Menu",
    tr: "Menü"
  },
  homeScreenLoadingWorkspaceText: {
    en: "Workspace is loading.",
    tr: "Çalışma alanı yükleniyor."
  },
  accountProfileModalTitle: {
    en: "Profile",
    tr: "Profil"
  },
  workspaceInfoListItemChangeWorkspace: {
    en: "Switch",
    tr: "Git"
  },
  workspaceInfoListItemWorkspaceSettings: {
    en: "Settings",
    tr: "Ayarlar"
  },
  workspaceSwitchModalTitle: {
    en: "Switch Workspace",
    tr: "Çalışma Alanını Değiştir"
  },
  moreMenuActionModalTitle: {
    en: "More",
    tr: "Daha Fazla"
  },
  workspaceMembersMoreMenuButton: {
    en: "Workspace Members",
    tr: "Çalışma Alanı Üyeleri"
  },
  workspaceSettingsMoreMenuButton: {
    en: "Workspace Settings",
    tr: "Çalışma Alanı Ayarları"
  },
  deviceOfflineModalText: {
    en: "Unable to connect internet",
    tr: "İnternet bağlantınız yok"
  },
  taskRelatedTaskNoAccess: {
    en: "You don't have access to this task.",
    tr: "Bu göreve erişim yetkiniz bulunmamaktadır."
  },
  teamSettingsScreenTaskVisibilitySectionTitle: {
    en: "Task Visibility",
    tr: "Görev Görünülürlüğü"
  },
  teamSettingsScreenTaskVisibilitySectionDescription: {
    en: "You can set who can list and see tasks.",
    tr: "Görevleri kimlerin listeleyip görüntüleyebileceğini ayarlayabilirsiniz."
  },
  teamSettingsScreenTaskVisibilitySectionSave: {
    en: "Save Changes",
    tr: "Değişiklikleri Kaydet"
  },
  teamTaskVisibility_VISIBLE_TO_ALL_TEAM_MEMBERS: {
    en: "Everyone on team",
    tr: "Takımdaki herkes"
  },
  teamTaskVisibility_OWNER_ASSIGNEE_AND_ADMINS: {
    en: "Owners & Assignees",
    tr: "Oluşturan & Atanan"
  },
  teamTaskVisibilityDetail_VISIBLE_TO_ALL_TEAM_MEMBERS: {
    en: "All team members can list all tasks within this team.",
    tr: "Takımdaki herkes takımdaki görevleri listeleyebilir."
  },
  teamTaskVisibilityDetail_OWNER_ASSIGNEE_AND_ADMINS: {
    en: "Tasks will be visibile to its owners, assignees and team admins.",
    tr: "Görevler yanlızca onları oluşturan, onlarin atandığı kişilerce ve takım yöneticileri tarafından görüntülenebilir."
  },
  genericYouNeedToUpgradePlanText: {
    en: "You need to upgrade your plan to use this feature.",
    tr: "Bu özelliği kullanabilmeniz için planınızı yükseltin."
  },
  loginScreenLoginWithGoogle: {
    en: "Login With Google",
    tr: "Google ile Giriş Yap"
  },
  teamSettingsScreenTeamStateSectionTitle_ARCHIVED: {
    en: "Unarchive Team",
    tr: "Takımı Tekrar Aktifleştir"
  },
  teamSettingsScreenTeamStateSectionDescription_ARCHIVED: {
    en: "You can continue to use this team as before by taking it out of the archive.",
    tr: "Bu takımı arşivden çıkararak eskisi gibi kullanmaya devam edebilirsiniz"
  },
  teamSettingsScreenTeamStateSectionTitle_ACTIVE: {
    en: "Archive Team",
    tr: "Takımı Arşive Kaldır"
  },
  teamSettingsScreenTeamStateSectionDescription_ACTIVE: {
    en: "Archive this team and make it read only.",
    tr: "Bu takımı arşivle ve salt okunur yap."
  },
  teamSettingsScreenTeamStateSectionActionButton_ARCHIVED: {
    en: "Activate",
    tr: "Aktifleştir"
  },
  teamSettingsScreenTeamStateSectionActionButton_ACTIVE: {
    en: "Archive",
    tr: "Arşivle"
  },
  sideMenuAddMailIntegrationLabel: {
    en: "Add Gmail Feed",
    tr: "Gmail Akışı Ekle"
  },
  newMailIntegrationModalTitle: {
    en: "New Gmail Integration",
    tr: "Yeni Gmail Entegrasyonu"
  },
  newMailIntegrationModalLabel: {
    en: "We carefully minimized the scope of access. All requested permissions are necessary for us to list your emails. Your emails are not recorded by Jinear, and they are not used for any data processing purposes.",
    tr: "Erişim kapsamını dikkatlice en aza indirdik. İstenen tüm izinler, maillerinizi listelememiz için gereklidir. Mailleriniz Jinear tarafından kayit altina alınmaz, hiçbir veri işleme için kullanılmazlar."
  },
  newMailIntegrationModalLabelSubtext: {
    en: "* We cache Gmail responses fully encrypted for a couple of minutes in order to avoid hitting Gmail API limits. (Typically 1-2 minutes)",
    tr: "* Gmail yanıtlarını tamamen şifreli bir şekilde birkaç dakika boyunca önbelleğe alıyoruz, böylece Gmail API sınırlarını aşmaktan kaçınıyoruz."
  },
  newMailIntegrationModalAddGmailLabel: {
    en: "Add Gmail Feed",
    tr: "Gmail Akışı Ekle"
  },
  sideMenuFeedsTitle: {
    en: "Your Feeds",
    tr: "Akışların"
  },
  sideMenuFeedMembers: {
    en: "Feed members",
    tr: "Akış üyeleri"
  },
  sideMenuFeedSettings: {
    en: "Feed settings",
    tr: "Akış ayarları"
  },
  feedPageTitle: {
    en: "Feed",
    tr: "Akış"
  },
  feedPageEmptyLabel: {
    en: "There isn't any content in this feed",
    tr: "Bu akışta herhangi bir öge bulunmuyor"
  },
  feedPageLoadMoreLabel: {
    en: "Load More",
    tr: "Daha Fazla"
  },
  feedItemLabelToday: {
    en: "Today",
    tr: "Bugün"
  },
  feedItemLabelYesterday: {
    en: "Yesterday",
    tr: "Dün"
  },
  feedLastUpdateLabel: {
    en: "Last update ${date}",
    tr: "Son güncelleme ${date}"
  },
  feedItemDetailCreateTask: {
    en: "Create A Task",
    tr: "Görev Oluştur"
  },
  feedItemDetailOpenInProvider_GOOGLE: {
    en: "Open in Gmail",
    tr: "Gmail de Görüntüle"
  },
  newTaskFormFeedItemDefaultTitle: {
    en: "Feed item added",
    tr: "Akış elemanı eklendi"
  },
  taskDetailFeedsTitle: {
    en: "Related Feed Items",
    tr: "İlgili Akış Elemanları"
  },
  deleteFeedCardTitle: {
    en: "Delete Feed",
    tr: "Akışı Sil"
  },
  deleteFeedCardText: {
    en: "Deleting feed does not deletes task related to them but removes relation.",
    tr: "Akışı silmek bu akışla bağlantılı görevleri silmez yanlızca aralarındaki bağı kopartır."
  },
  deleteFeedButton: {
    en: "Delete Feed",
    tr: "Akışı Sil"
  },
  deleteFeedCardAreYouSureTitle: {
    en: "Are you sure to delete this feed?",
    tr: "Bu akışı silmek istediğinize emin misiniz?"
  },
  deleteFeedCardAreYouSureText: {
    en: "This action can not be undone but you can always re-add feed.",
    tr: "Bu işlem geri alınamaz ancak her zaman tekrar akış ekleyebilirsiniz."
  },
  feedMemberListTitle: {
    en: "Feed Members",
    tr: "Akış Üyeleri"
  },
  feedMemberListText: {
    en: "Only members below and workspace admins can see feed content.",
    tr: "Yanlızca aşağıdaki akış üyeleri ve çalışma alanı yöneticileri akış içeriğine erişebilir."
  },
  feedMemberListItemOwner: {
    en: "Owner",
    tr: "Oluşturan"
  },
  feedMemberListItemUser: {
    en: "Member",
    tr: "Üye"
  },
  feedMemberListEmptyLabel: {
    en: "No feed member found.",
    tr: "Hiçbir üye bulunamadı."
  },
  deleteFeedMemberAreYouSureTitle: {
    en: "Kick Member",
    tr: "Üyeyi Akış Üyelerinden At"
  },
  deleteFeedMemberAreYouSureText: {
    en: "Are you sure to remove member access from this feed?",
    tr: "Üyeyi akış üyelerinden atmak istediğinize emin misiniz?"
  },
  deleteFeedMemberAreYouSureConfirmLabel: {
    en: "Kick",
    tr: "At"
  },
  feedMemberAddMember: {
    en: "Add Member",
    tr: "Üye Ekle"
  },
  workspaceMemberPickerModalTitle: {
    en: "Workspace Members",
    tr: "Çalışma Alanı Üyeleri"
  },
  workspaceMemberPickerModalFilterPlaceholder: {
    en: "Filter by username",
    tr: "Kullanıcı adına göre filtrele"
  },
  workspaceMemberPickerModalEmptyState: {
    en: "No workspace member found",
    tr: "Hiçbir çalışma alanı üyesi bulunamadı"
  },
  workspaceMemberPickerModalCancelButton: {
    en: "Cancel",
    tr: "Vazgeç"
  },
  workspaceMemberPickerModalSelectButton: {
    en: "Select",
    tr: "Seç"
  },
  calendarDayViewEmptyDayLabel: {
    en: "There isn't any task on this day.",
    tr: "Bugün için herhangi bir görev bulunamadı."
  },
  calendarDayViewNewTaskButton: {
    en: "New Task",
    tr: "Yeni Görev"
  },
  taskDetailCommentsNewComment: {
    en: "New Comment",
    tr: "Yeni Yorum"
  },
  taskNumbersOverviewTtitle: {
    en: "Overview",
    tr: "Genel Bakış"
  },
  taskNumbersOverviewTotalOpenTaskCount: {
    en: "Open",
    tr: "Açık"
  },
  taskNumbersOverviewTotalClosedTaskCount: {
    en: "Closed",
    tr: "Kapatılmış"
  },
  taskNumbersOverviewTotalTaskCount: {
    en: "Total",
    tr: "Toplam"
  },
  taskNumbersDeadlineComingUpCount: {
    en: "Deadline Coming Up",
    tr: "Son Tarihi Yaklaşanlar"
  },
  taskNumbersMissedDeadlineCount: {
    en: "Missed Deadline",
    tr: "Son Tarihi Geçen"
  },
  workflowStateGroupStats_BACKLOG: {
    en: "Backlog",
    tr: "Geri Plan"
  },
  workflowStateGroupStats_NOT_STARTED: {
    en: "Not Started",
    tr: "Henüz Başlamamış"
  },
  workflowStateGroupStats_STARTED: {
    en: "Started",
    tr: "Başlamış"
  },
  workflowStateGroupStats_COMPLETED: {
    en: "Completed",
    tr: "Tamamlanan"
  },
  workflowStateGroupStats_CANCELLED: {
    en: "Cancelled",
    tr: "Vazgeçildi"
  },
  workflowStateGroupStatsTitle: {
    en: "Workflow State Groups",
    tr: "İş Akışı Durumlarına Göre"
  },
  teamLongLastingTasksTitle: {
    en: "Oldest Waiting Tasks",
    tr: "Uzun Süredir Sonuçlanmamış Görevler"
  },
  teamLastActivitiesTitle: {
    en: "Last Activities",
    tr: "Son Aktiviteler"
  },
  teamHomePageTitle: {
    en: "Team ${teamName} Home",
    tr: "Genel Bakış - ${teamName}"
  },
  sideMenuTeamCalendarsTitle: {
    en: "Team Calendars",
    tr: "Ekip Takvimleri"
  },
  sideMenuExternalCalendarsTitle: {
    en: "External Calendars",
    tr: "Harici Takvimler"
  },
  sideMenuAddCalendarIntegrationLabel: {
    en: "Add Calendar",
    tr: "Takvim Ekle"
  },
  newCalendarIntegrationModalLabel: {
    en: "We carefully minimized the scope of access. All requested permissions are necessary for us to list your calendars. Your calendars and their events are not recorded by Jinear, and they are not used for any data processing purposes.",
    tr: "Erişim kapsamını dikkatlice en aza indirdik. İstenen tüm izinler, takvimlerinizi listelememiz için gereklidir. Takvimleriniz ve etkinlikleriniz Jinear tarafından kayit altina alınmaz, hiçbir veri işleme için kullanılmazlar."
  },
  newCalendarIntegrationModalLabelSubtext: {
    en: "* We cache Google Calendar responses fully encrypted for a couple of minutes in order to avoid hitting Google Calendar API limits. (Typically 1-2 minutes)",
    tr: "* Google Takvim yanıtlarını tamamen şifreli bir şekilde birkaç dakika boyunca önbelleğe alıyoruz, böylece Google Calendar API sınırlarını aşmaktan kaçınıyoruz."
  },
  newCalendarIntegrationModalAddGoogleCalendarLabel: {
    en: "Add Google Calendar",
    tr: "Google Takvim Ekle"
  },
  newCalendarIntegrationModalTitle: {
    en: "New Calendar Integration",
    tr: "Yeni Takvim Entegrasyonu"
  },
  unnamedCalendar: {
    en: "Unnamed Calendar",
    tr: "Adlandırılmamış Takvim"
  },
  sideMenuCalendarMembers: {
    en: "Calendar members",
    tr: "Takvim üyeleri"
  },
  sideMenuCalendarSettings: {
    en: "Calendar settings",
    tr: "Takvim ayarları"
  },
  calendarEventDetailDescription: {
    en: "No description",
    tr: "Açıklama girilmedi"
  },
  calendarEventDetailDescriptionEdit: {
    en: "Edit",
    tr: "Düzenle"
  },
  calendarEventDetailDescriptionSave: {
    en: "Save",
    tr: "Kaydet"
  },
  calendarEventDetailDescriptionCancel: {
    en: "Cancel",
    tr: "Vazgeç"
  },
  calendarEventTitleChangeModalTitle: {
    en: "Update Title",
    tr: "Başlığı Güncelle"
  },
  calendarEventTitleChangeModalInfoText: {
    en: "Please provide an another title for calendar event.",
    tr: "Takvim etkinliği için yeni bir başlık girin."
  },
  calendarEventModalGoToSourceLink: {
    en: "Open External Link",
    tr: "Kaynağında Görüntüle"
  },
  calendarEventCalendarExternalSourceChange: {
    en: "Change Calendar",
    tr: "Takvimi Değiştir"
  },
  genericDateButtonLabel: {
    en: "Pick A Date",
    tr: "Bir Tarih Seçin"
  },
  calendarEventAllDayButtonLabel: {
    en: "All Day",
    tr: "Tüm Gün"
  },
  calendarEventSpesificDatesButtonLabel: {
    en: "Spesific Dates",
    tr: "Spesifik Tarih"
  },
  calendarEventDateStart: {
    en: "Starts",
    tr: "Başlangıç"
  },
  calendarEventDateEnds: {
    en: "Ends",
    tr: "Bitiş"
  },
  calendarDescriptionSaving: {
    en: "Saving",
    tr: "Kayıt ediliyor"
  },
  calendarTitleSaving: {
    en: "Saving",
    tr: "Kayıt ediliyor"
  },
  calendarDatesSaveButton: {
    en: "Save",
    tr: "Kaydet"
  },
  calendarDatesCancelButton: {
    en: "Cancel",
    tr: "Vazgeç"
  },
  calendarDatesSaving: {
    en: "Saving",
    tr: "Kayıt ediliyor"
  },
  assignedToMeClearFilterButton: {
    en: "Clear Filter",
    tr: "Filtreyi Kaldır"
  },
  assignedToMeFilterButton: {
    en: "Filter",
    tr: "Filtrele"
  },
  calendarSourceButtonHide: {
    en: "Hide",
    tr: "Gizle"
  },
  calendarMemberListTitle: {
    en: "Calendar Members",
    tr: "Takvim Üyeleri"
  },
  calendarMemberListText: {
    en: "Only members below and workspace admins can see calendar content.",
    tr: "Yanlızca aşağıdaki takvim üyeleri ve çalışma alanı yöneticileri takvim içeriğine erişebilir."
  },
  calendarMemberAddMember: {
    en: "Add Member",
    tr: "Üye Ekle"
  },
  calendarMemberListEmptyLabel: {
    en: "No calendar member found.",
    tr: "Hiçbir üye bulunamadı."
  },
  deleteCalendarMemberAreYouSureTitle: {
    en: "Kick Member",
    tr: "Üyeyi Takvim Üyelerinden At"
  },
  deleteCalendarMemberAreYouSureText: {
    en: "Are you sure to remove member access from this calendar?",
    tr: "Üyeyi takvim üyelerinden atmak istediğinize emin misiniz?"
  },
  deleteCalendarMemberAreYouSureConfirmLabel: {
    en: "Kick",
    tr: "At"
  },
  calendarMemberListItemOwner: {
    en: "Owner",
    tr: "Oluşturan"
  },
  calendarMemberListItemUser: {
    en: "Member",
    tr: "Üye"
  },
  activeCalendarMemberKick: {
    en: "Kick Member",
    tr: "Üyeyi At"
  },
  deleteCalendarCardAreYouSureTitle: {
    en: "Are you sure to delete this calendar?",
    tr: "Bu takvimi silmek istediğinize emin misiniz?"
  },
  deleteCalendarCardAreYouSureText: {
    en: "This action can not be undone but you can always re-add calendar.",
    tr: "Bu işlem geri alınamaz ancak her zaman tekrar takvim ekleyebilirsiniz."
  },
  deleteCalendarButton: {
    en: "Delete Calendar",
    tr: "Takvimi Sil"
  },
  deleteCalendarCardTitle: {
    en: "Delete Calendar",
    tr: "Takvimi Sil"
  },
  deleteCalendarCardText: {
    en: "Deleting the calendar does not delete the calendar from the original source and the events associated with this calendar, it only disconnects it from Jinear.",
    tr: "Takvimi silmek orijinal kaynaktaki takvimi ve bu takvim ile bağlantılı etkinlikleri silmez yanlızca Jinear ile bağı kopartır."
  },
  homePageFeatureTitle_Calendar: {
    en: "First class calendar experience to manage your tasks not just a Google Calendar skin",
    tr: "Görevlerinizi takip etmek için kaliteli bir takvim deneyimi"
  },
  homePageFeatureText_Calendar: {
    en: "Manage your or your teams tasks in calendar. You can always integrate Google Calendar and grant access to your teammates.",
    tr: "Sizin veya takımınızın görevlerini takvim üzerinden takip edin. Ayrıca Google Takvim deki takvimlerinizi bağlayıp isterseniz takım arkadaslarınıza da erisim yetkisi verebilirsiniz."
  },
  homePageFeatureTitle_Task: {
    en: "Powerful task management",
    tr: "Zengin görev yönetimi"
  },
  homePageFeatureText_Task: {
    en: "Create tasks and assign team mates, set planned or due dates, comment, attach files, add reminders, link other tasks.",
    tr: "Yarattığınız görevleri takım arkadaşlarınıza atayın, yapılacak tarihi veya son tarihi girin, yorumlar ekleyin, dosya iliştirin, anımsatıcılar oluşturun, diğer görevler ile ilişkilendirin."
  },
  homePageFeatureTitle_Feed_GMail: {
    en: "Integrate Gmail And Create Feeds & Tasks",
    tr: "Gmail'i entegre edin, Akış ve Görevler oluşturun"
  },
  homePageFeatureText_Feed_Mail: {
    en: "Useful for initializing 8tasks directly from inbox. You can add your company’s mail like support@yourcompany.com to feed and add team mates to it so they can view incoming requests and take action. Or add your personal mail for personal use and create tasks.",
    tr: "Gelen kutusundan doğrudan görevler başlatmak için kullanışlıdır. destek@şirketiniz.com gibi şirketinizin posta adresininden bir akış oluşturabilir ve ekibinizi ekleyebilirsiniz, böylece ilgili ekip üyeleri gelen talepleri görebilir ve harekete geçebilirler. Veya kişisel kullanım için kişisel postanızı ekleyin ve görevler oluşturun"
  },
  calendarEventsShareableKeyModalTitle: {
    en: "Public Address in iCal Format",
    tr: "iCal Biçimindeki Herkese Açık Adres"
  },
  calendarEventsShareableKeyModalText: {
    en: "Use this address to access the calendar of events in this workspace from other applications.",
    tr: "Bu çalışma alanındaki etkinlikler takvimine başka uygulamalardan erişmek için bu adresi kullanın."
  },
  calendarEventsShareableKeyModalSubText: {
    en: "This adress generated only for you. Anyone with this link can list your events so do not share it with anyone. You can re-generate if you share link accidentally.",
    tr: "Bu adres yalnızca sizin için oluşturuldu. Bu bağlantıya sahip herkes etkinliklerinizi listeleyebilir, bu yüzden kimseyle paylaşmayın. Bağlantıyı yanlışlıkla paylaşırsanız yeniden oluşturabilirsiniz."
  },
  calendarEventsShareableKeyModalCopy: {
    en: "Copy Link",
    tr: "Linki Kopyala"
  },
  accountDeleteButtonLabel: {
    en: "Delete Account",
    tr: "Hesabı Sil"
  },
  accountDeleteAreYouSureTitle: {
    en: "Are you sure to delete account?",
    tr: "Hesabı silmek istediğine emin misin?"
  },
  accountDeleteAreYouSureText: {
    en: "We will send you a confirmation email. If you choose to follow the link provided in the email, your account and personal information will be deleted. This action can not be undone.",
    tr: "Sana bir onay e-postası göndereceğiz. E-postada verdiğimiz bağlantıyı takip ederseniz, hesabınız ve kişisel bilgileriniz silinecektir. Bu işlem geri alınamaz."
  },
  accountDeleteAreYouSureTextWithoutEmailConfirm: {
    en: "Your account and personal information will be deleted. This action can not be undone.",
    tr: "Hesabınız ve kişisel bilgileriniz silinecektir. Bu işlem geri alınamaz."
  },
  accountDeleteAreYouSureConfirmLabel: {
    en: "Confirm",
    tr: "Onaylıyorum"
  },
  accountDeleteConfirmPageSuccessTitle: {
    en: "Your account has been deleted successfully",
    tr: "Hesabın başarıyla silindi"
  },
  accountDeleteConfirmPageFailureTitle: {
    en: "Account deletion has failed",
    tr: "Hesap silme başarısız"
  },
  accountDeleteConfirmPageFailureText: {
    en: "Your request might be timed out. Please try again later or contact us.",
    tr: "İsteğiniz zaman aşımına uğramış olabilir. Lütfen daha sonra tekrar deneyin veya bizimle irtibata geçin."
  },
  accountDeletionMailSendSuccessfully: {
    en: "Account deletion email successfully sent.",
    tr: "Hesap silme e-postası başarıyla gönderildi."
  },
  accountDeletedSuccessfully: {
    en: "Account successfully deleted.",
    tr: "Hesap başarıyla silindi."
  },
  calendarSyncing: {
    en: "Syncing",
    tr: "Eşitleniyor"
  },
  sideMenuChannelsTitle: {
    en: "Channels",
    tr: "Kanallar"
  },
  channelHeaderMembersMore: {
    en: "...${number} more",
    tr: "...${number} daha"
  },
  channelSettingsModalMembersTab: {
    en: "Members",
    tr: "Üyeler"
  },
  channelMemberRole_OWNER: {
    en: "Owner",
    tr: "Kanal Sahibi"
  },
  channelMemberRole_ADMIN: {
    en: "Admin",
    tr: "Yönetici"
  },
  channelMemberRole_MEMBER: {
    en: "Member",
    tr: "Üye"
  },
  channelMemberRole_ROBOT: {
    en: "Robot",
    tr: "Robot"
  },
  deleteChannelMemberAreYouSureTitle: {
    en: "Kick Member",
    tr: "Üyeyi Kanaldan At"
  },
  deleteChannelMemberAreYouSureText: {
    en: "Are you sure to kick member from this channel?",
    tr: "Üyeyi bu kanaldan atmak istediğinize emin misiniz?"
  },
  deleteChannelMemberAreYouSureConfirmLabel: {
    en: "Kick",
    tr: "At"
  },
  addMemberToChannel: {
    en: "Add Member",
    tr: "Üye Ekle"
  },
  leaveChannel: {
    en: "Leave Channel",
    tr: "Kanaldan Ayrıl"
  },
  channelGeneralInfoChannelTitle: {
    en: "Channel Title",
    tr: "Kanal Başlığı"
  },
  channelSettingsModalInfoTab: {
    en: "Info",
    tr: "Genel"
  },
  channelTitleChangeModalTitle: {
    en: "Update Title",
    tr: "Başlığı Güncelle"
  },
  channelTitleChangeModalInfoText: {
    en: "Please provide an another title for channel.",
    tr: "Kanal için yeni bir başlık girin."
  },
  channelGeneralInfoChanelVisibility: {
    en: "Channel Visibility",
    tr: "Kanal Görünülürlüğü"
  },
  channelGeneralInfoChanelVisibility_EVERYONE: {
    en: "Everyone",
    tr: "Herkes"
  },
  channelGeneralInfoChanelVisibility_MEMBERS_ONLY: {
    en: "Channel Members",
    tr: "Kanal Üyeleri"
  },
  channelGeneralInfoChanelVisibility_PUBLIC_WITH_GUESTS: {
    en: "Public with guests",
    tr: "Ziyaretçiler Dahil Herkese"
  },
  channelGeneralInfoChanelVisibility_description_EVERYONE: {
    en: "Everyone in workspace can list and join this channel.",
    tr: "Çalışma alanı içerisindeki herkes"
  },
  channelGeneralInfoChanelVisibility_description_MEMBERS_ONLY: {
    en: "Only channel members can see this channel.",
    tr: "Yanlızca kanal üyeleri kanalı görebilir."
  },
  channelGeneralInfoChanelVisibility_description_PUBLIC_WITH_GUESTS: {
    en: "Channel is public everyone including guests can see this channel.",
    tr: "Kanal herkese açık, ziyaretçiler dahil kanal içeriği görüntülenebilir."
  },
  channelGeneralInfoChanelParticipation: {
    en: "Channel Participation",
    tr: "Mesaj Gönderimi"
  },
  channelGeneralInfoChanelParticipation_EVERYONE: {
    en: "Everyone in Channel",
    tr: "Kanaldaki Herkes"
  },
  channelGeneralInfoChanelParticipation_ADMINS_CAN_START_CONVERSATION_EVERYONE_CAN_REPLY: {
    en: "Limited Replies",
    tr: "Limitli Cevaplandırma"
  },
  channelGeneralInfoChanelParticipation_READ_ONLY: {
    en: "Ready Only",
    tr: "Salt Okunur"
  },
  channelGeneralInfoChanelParticipation_description_EVERYONE: {
    en: "Everyone in channel can start thread and reply.",
    tr: "Kanaldaki herkes sohbet başlatabilir ve cevaplayabilir. "
  },
  channelGeneralInfoChanelParticipation_description_ADMINS_CAN_START_CONVERSATION_EVERYONE_CAN_REPLY: {
    en: "Admins can start conversation, everyone on channel can reply.",
    tr: "Yöneticiler sohbet başlatabilir, kanaldaki herkes cevaplayabilir."
  },
  channelGeneralInfoChanelParticipation_description_READ_ONLY: {
    en: "Only admins can post and reply.",
    tr: "Yanlızca yöneticiler gönderi oluşturabilir."
  },
  newThreadInputPlaceholder: {
    en: "Start a new thread...",
    tr: "Yeni bir gönderi oluşturun..."
  },
  newThreadButtonLabel: {
    en: "Post",
    tr: "Gönder"
  },
  newThreadFloatingButtonLabel: {
    en: "New Thread",
    tr: "Yeni Gönderi"
  },
  threadReplyMessageInputButton: {
    en: "Reply",
    tr: "Cevapla"
  },
  threadReplyMessageInputPlaceholder: {
    en: "Reply with a message",
    tr: "Bir mesaj ile cevapla"
  },
  threadMessageRemainingMessageCount: {
    en: "...{count} more messages",
    tr: "...{count} mesaj daha"
  },
  threadMessageListLoadMore: {
    en: "Show older",
    tr: "Önceki mesajları görüntüle"
  },
  threadsLoadMore: {
    en: "Show older",
    tr: "Önceki gönderileri görüntüle"
  },
  threadsEmpty: {
    en: "There isn't any threads yet.",
    tr: "Herhangi bir gönderi bulunamadı."
  },
  newChannelButton: {
    en: "Create Channel",
    tr: "Kanal Oluştur"
  },
  newChannelModalTitle: {
    en: "Create Channel",
    tr: "Kanal Oluştur"
  },
  newChannelModalFormChannelTitle: {
    en: "Channel Title",
    tr: "Kanal Başlığı"
  },
  newChannelModalFormSubmit: {
    en: "Create Channel",
    tr: "Kanalı Oluştur"
  },
  sideMenuConversations: {
    en: "Conversations",
    tr: "Sohbetler"
  },
  newConversationButton: {
    en: "New Conversation",
    tr: "Yeni Mesaj"
  },
  conversationsScreenTitle: {
    en: "Conversation Settings",
    tr: "Sohbet Ayarları"
  },
  conversationLoadMore: {
    en: "Show older",
    tr: "Önceki mesajları görüntüle"
  },
  conversationEmpty: {
    en: "There isn't any messages yet.",
    tr: "Herhangi bir mesaj bulunamadı."
  },
  joinChannelButton: {
    en: "Join Channel (${number})",
    tr: "Kanala Katıl (${number})"
  },
  channelListModalTitle: {
    en: "Channels",
    tr: "Kanallar"
  },
  channelListModalJoinChannel: {
    en: "Join",
    tr: "Katıl"
  },
  newConversationModalTitle: {
    en: "New Conversation",
    tr: "Yeni Sohbet"
  },
  workspaceMemberInputPickerModalEmptyState: {
    en: "No workspace member found",
    tr: "Hiçbir çalışma alanı üyesi bulunamadı"
  },
  newConversationSend: {
    en: "Send",
    tr: "Gönder"
  },
  newConversationSendToEmpty: {
    en: "Please select one or more member",
    tr: "Mesaj gönderilecek kişi seçin"
  },
  conversationPageEmptyStateSubTitle: {
    en: "No conversation selected.",
    tr: "Herhangi bir sohbet seçilmedi."
  },
  conversationPageEmptyStateText: {
    en: "Please select a conversation from the list or initiate a new one to begin your communication. Streamline your business interactions with seamless messaging.",
    tr: "Lütfen listeden bir konuşma seçin veya yeni bir konuşma başlatarak iletişime geçin."
  },
  selfHostWaitlistModalTitle: {
    en: "Let us inform you",
    tr: "Size haber verelim"
  },
  selfHostWaitlistModalInfoText: {
    en: "We're working on self hosting option. Please provide an email address and we let you know when it's ready.",
    tr: "Self-hosting seçeneği üzerinde çalışıyoruz. Hazır olduğunda size haber vermemiz için lütfen e-posta adresinizi girin."
  },
};

export default translations;