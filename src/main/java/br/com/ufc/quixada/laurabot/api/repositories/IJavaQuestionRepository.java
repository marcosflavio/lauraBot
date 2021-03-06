package br.com.ufc.quixada.laurabot.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.ufc.quixada.laurabot.api.model.JavaQuestion;

@Repository
public interface IJavaQuestionRepository extends JpaRepository<JavaQuestion, Long> {
	
	public List<JavaQuestion> findByUserIsNotNull();
}
