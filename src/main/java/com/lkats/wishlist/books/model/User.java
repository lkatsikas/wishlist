package com.lkats.wishlist.books.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class User {

    @Id
    private String id;

    @JsonIgnore
    private String password;

    @Email
    private String email;

    private String username;
    private boolean active = true;
    private Set<String> roles;
}
