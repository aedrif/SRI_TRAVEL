package org.example.sri.repo;

import org.example.sri.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    HashSet<String> findAllByCategory(String s);

    @Query("SELECT e.word FROM Token e WHERE e.category = :value")
    HashSet<String> findWordsByCondition(@Param("value") String value);

}
