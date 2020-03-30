package com.example.springbootcassandra;

import lombok.Getter;
import lombok.ToString;
import lombok.Value;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("people_by_first_name")
@Getter
@Value
@ToString
public class Person {

  @PrimaryKey
  private PersonKey key;

  @Column("last_name")
  private String lastName;

  @Column
  private double salary;

  public Person(final PersonKey key, final String lastName, final double salary) {
    this.key = key;
    this.lastName = lastName;
    this.salary = salary;
  }
}
