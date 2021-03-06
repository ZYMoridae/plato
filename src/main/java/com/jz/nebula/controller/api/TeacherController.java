package com.jz.nebula.controller.api;

import com.jz.nebula.entity.Role;
import com.jz.nebula.entity.teacher.Teacher;
import com.jz.nebula.entity.teacher.TeacherMeta;
import com.jz.nebula.entity.teacher.TeacherSubscription;
import com.jz.nebula.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;

    @GetMapping
    @RolesAllowed({Role.ROLE_ADMIN, Role.ROLE_USER, Role.ROLE_VENDOR, Role.ROLE_TEACHER})
    public @ResponseBody
    PagedModel<EntityModel<Teacher>> all(@RequestParam String keyword, Pageable pageable,
                                         final UriComponentsBuilder uriBuilder, final HttpServletResponse response,
                                         PagedResourcesAssembler<Teacher> assembler) {
        return teacherService.findAll(keyword, pageable, assembler);
    }

    @GetMapping("/{id}")
    @RolesAllowed({Role.ROLE_USER, Role.ROLE_VENDOR, Role.ROLE_ADMIN})
    public @ResponseBody
    HashMap findById(@PathVariable("id") long id) {
        return teacherService.findById(id);
    }

    @GetMapping("/{id}/meta")
    @RolesAllowed({Role.ROLE_USER, Role.ROLE_VENDOR, Role.ROLE_ADMIN})
    public @ResponseBody
    TeacherMeta findMetaById(@PathVariable("id") long id) {
        return teacherService.findMetaByTeacherId(id);
    }

    @PostMapping("/{id}/meta")
    @RolesAllowed({Role.ROLE_USER, Role.ROLE_VENDOR, Role.ROLE_ADMIN})
    public @ResponseBody
    TeacherMeta createMeta(@PathVariable("id") long id, @RequestBody TeacherMeta teacherMeta) throws Exception {
        return teacherService.saveTeacherMeta(id, teacherMeta);
    }

    @PutMapping("/{id}/meta")
    @RolesAllowed({Role.ROLE_TEACHER, Role.ROLE_ADMIN})
    public @ResponseBody
    TeacherMeta updateMeta(@PathVariable("id") long id, @RequestBody TeacherMeta teacherMeta) throws Exception {
        return teacherService.saveTeacherMeta(id, teacherMeta);
    }

    @DeleteMapping("/{id}/meta")
    @RolesAllowed({Role.ROLE_TEACHER, Role.ROLE_ADMIN})
    public @ResponseBody
    ResponseEntity<?> updateMeta(@PathVariable("id") long id) {
        teacherService.deleteTeacherMeta(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/subscribe")
    @RolesAllowed({Role.ROLE_USER})
    public @ResponseBody
    TeacherSubscription subscribe(@PathVariable("id") long id, @RequestBody TeacherSubscription teacherSubscription) {
        return teacherService.subscribeTeacher(teacherSubscription);
    }

    @PostMapping("/{id}/unsubscribe")
    @RolesAllowed({Role.ROLE_USER})
    public @ResponseBody
    ResponseEntity<?> unsubscribe(@PathVariable("id") long id) {
        teacherService.unsubscribeTeacher(id);
        return ResponseEntity.noContent().build();
    }

}
