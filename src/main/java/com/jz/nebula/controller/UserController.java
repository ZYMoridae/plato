package com.jz.nebula.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jz.nebula.auth.IAuthenticationFacade;
import com.jz.nebula.dao.RoleRepository;
import com.jz.nebula.dao.UserRepository;
import com.jz.nebula.dao.UserRolesRepository;
import com.jz.nebula.entity.Role;
import com.jz.nebula.entity.User;
import com.jz.nebula.entity.UserRole;
import com.jz.nebula.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    TokenService jwtTokenProvider;

    @Autowired
    UserRolesRepository userRolesRepository;

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    /**
     *
     * @param userDetails
     * @return
     */
    @GetMapping("/me")
    public ResponseEntity<?> currentUser(@AuthenticationPrincipal UserDetails userDetails) {
        Map<Object, Object> model = new HashMap<>();
        System.out.println(userDetails.getUsername());
        model.put("username", userDetails.getUsername());
        model.put("roles", userDetails.getAuthorities().stream().map(a -> ((GrantedAuthority) a).getAuthority())
                .collect(toList()));

        User user = userRepository.findByUsername(userDetails.getUsername()).get();

        return ok(user);
    }

    /**
     * This is public route
     *
     * @param user
     * @return
     */
    @PostMapping("")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        // TODO: Move logic into user service
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        ArrayList<Role> rolesList = new ArrayList<>();
        Role normalUser = roleRepository.findByCode(Role.ROLE_USER.replaceAll("ROLE_", "")).get();

        rolesList.add(normalUser);
        user.setRoles(rolesList);
        ObjectMapper oMapper = new ObjectMapper();
        User savedUser = userRepository.save(user);

        UserRole userRole = new UserRole();
        userRole.setRoleId(normalUser.getId());
        userRole.setUserId(savedUser.getId());
        userRole = userRolesRepository.save(userRole);

//        Set<UserRole> userRoleList = new HashSet<>();
//        userRoleList.add(userRole);
//        user.setUserRoles(userRoleList);

        savedUser = userRepository.findById(savedUser.getId()).get();

        @SuppressWarnings("unchecked")
        Map<String, Object> map = oMapper.convertValue(savedUser, Map.class);
        List<String> roles = new ArrayList<>();
        roles.add(Role.ROLE_USER);
        Map<String, String> tokenMap = jwtTokenProvider.createToken(savedUser.getUsername(), rolesList);
        map.put("token", tokenMap.get("accessToken"));
        map.put("refreshToken", tokenMap.get("refreshToken"));

        return ok(map);
    }

    /**
     *
     * @param id
     * @param user
     * @return
     * @throws Exception
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") long id, @RequestBody User user) throws Exception {
        if (id != authenticationFacade.getUser().getId()) {
            throw new Exception();
        }
        return ok(userRepository.save(user));
    }

}
