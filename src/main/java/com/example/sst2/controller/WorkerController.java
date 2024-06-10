package com.example.sst2.controller;

import com.example.sst2.dto.AuthenticationDto;
import com.example.sst2.dto.WorkerRegistrationDto;
import com.example.sst2.dto.WorkerUpdateDto;
import com.example.sst2.exceptions.WorkerException;
import com.example.sst2.model.Worker;
import com.example.sst2.security.util.JwtUtil;
import com.example.sst2.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/api")
public class WorkerController {
    private final WorkerService workerService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @PreAuthorize("hasAuthority('READ')")
    @GetMapping("/workers")
    public List<Worker> findAll(@CurrentSecurityContext SecurityContext context, @AuthenticationPrincipal UserDetails userDetails) {
        System.out.println(userDetails);
        System.out.println("----------");
        System.out.println(context);
        return workerService.findAll();
    }

    @PreAuthorize("hasAuthority('READ')")
    @GetMapping("/worker/{id}")
    public ResponseEntity<Worker> findById(@PathVariable Long id) {
        Worker worker = workerService.findById(id);
        return new ResponseEntity<>(worker, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('WRITE')")
    @PostMapping("/worker")
    public ResponseEntity<Worker> save(@RequestBody @Valid WorkerRegistrationDto workerDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new WorkerException("ошибка сохранения, неправильно указаны данные в dto");
        }
        return new ResponseEntity<>(workerService.save(workerDto), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('UPDATE')")
    @PutMapping("/worker/{id}")
    public ResponseEntity<Worker> update(@PathVariable Long id, @RequestBody @Valid WorkerUpdateDto workerUpdateDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new WorkerException("ошибка обновления, неправильно указаны данные в dto");
        }
        return new ResponseEntity<>(workerService.update(id, workerUpdateDto), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('DELETE')")
    @DeleteMapping("/worker/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        workerService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/worker/registration")
    public Map<String, String> registration(@RequestBody @Valid WorkerRegistrationDto workerRegistrationDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new WorkerException("ошибка валидации");
        }
        if (workerService.findByName(workerRegistrationDto.getName()).isPresent()) {
            throw new WorkerException("пользователь с таким именем уже существует!");
        }
        workerService.save(workerRegistrationDto);
        String token = jwtUtil.generateToken(workerRegistrationDto.getName());
        return Map.of("ваш токен", token);
    }

    @PostMapping("/worker/login")
    public Map<String, String> login(@RequestBody @Valid AuthenticationDto authenticationDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new WorkerException("ошибка валидации в dto");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                authenticationDto.getUsername(), authenticationDto.getPassword()
        );

        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (AuthenticationException e) {
            System.out.println(e.getMessage());
        }
        String token = jwtUtil.generateToken(authenticationToken.getName());
        return Map.of("ваш токен", token);
    }

}
