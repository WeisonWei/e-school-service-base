package com.es.base.common;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;


@NoRepositoryBean
public interface CommRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    @Override
    @PostMapping("/save")
    <S extends T> S save(@RequestBody S entity);

    @Override
    @PostMapping("/saveAll")
    <S extends T> List<S> saveAll(@RequestBody Iterable<S> entitis);

    @Override
    @GetMapping("/findById")
    Optional<T> findById(@RequestParam ID id);

    @Override
    @GetMapping("/existsById")
    boolean existsById(@RequestParam ID id);

    @Override
    @GetMapping("/findAllById")
    List<T> findAllById(@RequestBody Iterable<ID> ids);

    @Override
    @GetMapping("/findAll")
    Page<T> findAll(@PageableDefault(page = 0, size = 10) Pageable pageable);

    @Override
    @GetMapping("/count")
    long count();

}
