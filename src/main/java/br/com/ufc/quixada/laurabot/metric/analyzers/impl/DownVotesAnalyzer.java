package br.com.ufc.quixada.laurabot.metric.analyzers.impl;

import org.springframework.stereotype.Component;

import br.com.ufc.quixada.laurabot.api.model.JavaAnswer;
import br.com.ufc.quixada.laurabot.api.model.JavaQuestion;
import br.com.ufc.quixada.laurabot.metric.analyzers.AnswerAnalyzer;
import br.com.ufc.quixada.laurabot.metric.model.LauraAnswerMetric;

@Component
public class DownVotesAnalyzer implements AnswerAnalyzer {

	@Override
	public void analyze(JavaQuestion javaQuestion, JavaAnswer javaAnswer, LauraAnswerMetric lauraAnswerMetric) {

	}

}