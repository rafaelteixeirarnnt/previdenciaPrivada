package br.com.quality.builder;

import org.primefaces.model.chart.BarChartModel;

import br.com.quality.vo.CalculoPrevidenciaVO;

public class CalculoPrevidenciaBuilder {

	private String nome;

	private Double salario;

	private Double contribuicaoX;

	private Double contribuicaoY;

	private Integer tempoContribuicao;
	
	private BarChartModel graficoAnimado;

	public CalculoPrevidenciaBuilder comNome(String nome) {
		this.nome = nome;
		return this;
	}

	public CalculoPrevidenciaBuilder comSalario(Double salario) {
		this.salario = salario;
		return this;
	}

	public CalculoPrevidenciaBuilder comContribuicaoX(Double contribuicaoX) {
		this.contribuicaoX = contribuicaoX;
		return this;
	}

	public CalculoPrevidenciaBuilder comContribuicaoY(Double contribuicaoY) {
		this.contribuicaoY = contribuicaoY;
		return this;
	}

	public CalculoPrevidenciaBuilder comTempoContribuicao(Integer tempoContribuicao) {
		this.tempoContribuicao = tempoContribuicao;
		return this;
	}
	
	public CalculoPrevidenciaBuilder comGraficoAnimado(BarChartModel graficoAnimado){
		this.graficoAnimado = graficoAnimado;
		return this;
	}
	
	public CalculoPrevidenciaVO gerarCalculoPrevidenciaVO(){
		return new CalculoPrevidenciaVO(nome, salario, contribuicaoX, contribuicaoY, tempoContribuicao, graficoAnimado);
	}

}