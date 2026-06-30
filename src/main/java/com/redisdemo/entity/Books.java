package com.redisdemo.entity;

import com.redisdemo.dto.Auditable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Table(name = "books")
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Books extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "book_id")
    private UUID bookId;
    @Column(name = "book_name",length = 50,nullable = false)
    private String bookName;
    @Column(name = "author_name", length = 50, nullable = false)
    private String authorName;

}
