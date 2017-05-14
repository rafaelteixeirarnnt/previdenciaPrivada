package br.com.quality.vo;

import org.primefaces.model.chart.BarChartModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalculoPrevidenciaVO {

	private String nome;

	private Double salario;

	private Double contribuicaoX;

	private Double contribuicaoY;

	private Integer tempoContribuicao;

	private BarChartModel graficoAnimado;

	public CalculoPrevidenciaVO(String nome, Double salario, Double contribuicaoX, Double contribuicaoY,
			Integer tempoContribuicao, BarChartModel graficoAnimado) {
		this.nome = nome;
		this.salario = salario;
		this.contribuicaoX = contribuicaoX;
		this.contribuicaoY = contribuicaoY;
		this.tempoContribuicao = tempoContribuicao;
		this.graficoAnimado = graficoAnimado;

	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Double getSalario() {
		return salario;
	}

	public void setSalario(Double salario) {
		this.salario = salario;
	}

	public Double getContribuicaoX() {
		return contribuicaoX;
	}

	public void setContribuicaoX(Double contribuicaoX) {
		this.contribuicaoX = contribuicaoX;
	}

	public Double getContribuicaoY() {
		return contribuicaoY;
	}

	public void setContribuicaoY(Double contribuicaoY) {
		this.contribuicaoY = contribuicaoY;
	}

	public Integer getTempoContribuicao() {
		return tempoContribuicao;
	}

	public void setTempoContribuicao(Integer tempoContribuicao) {
		this.tempoContribuicao = tempoContribuicao;
	}

	public BarChartModel getGraficoAnimado() {
		return graficoAnimado;
	}

	public void setGraficoAnimado(BarChartModel graficoAnimado) {
		this.graficoAnimado = graficoAnimado;
	}

}