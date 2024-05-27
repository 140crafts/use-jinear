package co.jinear.core.controller;

import co.jinear.core.model.entity.account.Account;
import co.jinear.core.repository.AccountRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(value = "/debug")
@RequiredArgsConstructor
public class DebugController {

    private final AccountRepository accountRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void debug(HttpEntity<String> httpEntity) {
    }

    @GetMapping
    public Object debug2(HttpServletResponse response) throws Exception {
        Optional<Account> optional = accountRepository.findById("01htc6dd4jt4gasr6qepejyz3b");
        System.out.println(optional);
        return null;
    }
}
