package com.example.sst2.service;

import com.example.sst2.dto.WorkerRegistrationDto;
import com.example.sst2.dto.WorkerUpdateDto;
import com.example.sst2.model.Role;
import com.example.sst2.model.Worker;
import com.example.sst2.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkerService {
    private final WorkerRepository workerRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public List<Worker> findAll() {
        return workerRepository.findAll();
    }

    public Worker findById(long id) {
        return workerRepository.findById(id).get();
    }

    public Worker save(WorkerRegistrationDto workerDto) {
        Worker worker = modelMapper.map(workerDto, Worker.class);
        worker.setPassword(passwordEncoder.encode(workerDto.getPassword()));
        worker.setRole(Role.WORKER);
        return workerRepository.save(worker);
    }

    public Worker update(Long id, WorkerUpdateDto workerUpdateDto) {
        Worker worker = workerRepository.findById(id).get();
        worker.setName(worker.getName());
        worker.setAge(worker.getAge());
        worker.setPassword(passwordEncoder.encode(workerUpdateDto.getPassword()));
        worker.setRole(workerUpdateDto.getRoles());
        return workerRepository.save(worker);
    }

    public void delete(Long id) {
        workerRepository.deleteById(id);
    }

    public Optional<Worker> findByName(String name) {
      return workerRepository.findByName(name);
    }
}
