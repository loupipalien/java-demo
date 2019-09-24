package com.ltchen.demo.spring.data.jpa;

import lombok.*;

import javax.persistence.*;

/**
 * @author: 01139983
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "t_customer")
public class Customer {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "first_name")
    @NonNull
    private String firstName;

    @Column(name = "last_name")
    @NonNull
    private String lastName;

}
