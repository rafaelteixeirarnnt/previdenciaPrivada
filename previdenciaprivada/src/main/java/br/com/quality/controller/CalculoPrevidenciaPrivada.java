package br.com.quality.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LegendPlacement;

import br.com.quality.builder.CalculoPrevidenciaBuilder;
import br.com.quality.builder.FundosBuilder;
import br.com.quality.util.Constantes;
import br.com.quality.vo.CalculoPrevidenciaVO;
import br.com.quality.vo.FundosVO;
import br.com.quality.vo.ProjecaoVO;
import lombok.Getter;
import lombok.Setter;

@ManagedBean
@RequestScoped
public class CalculoPrevidenciaPrivada implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private CalculoPrevidenciaVO calculoPrevidenciaVO;
	

	@PostConstruct
	public void inicializar() {
		CalculoPrevidenciaBuilder builder = new CalculoPrevidenciaBuilder();
		calculoPrevidenciaVO = builder.comNome("").comContribuicaoX(new Double(0)).comContribuicaoY(new Double(0))
				.comSalario(new Double(0)).comTempoContribuicao(new Integer(2)).comGraficoAnimado(new BarChartModel())
				.gerarCalculoPrevidenciaVO();
	}

	public void calcular() {
		Double reajusteAnual = new Double(Constantes.REAJUSTE_ANUAL);
		FundosBuilder fundosBuilder = new FundosBuilder();
		Double salarioExcedente = new Double(calculoPrevidenciaVO.getSalario() * (50.0 / 100));
		Double reajusteSalarial = new Double(0);
		FundosVO fundosVO = fundosBuilder.comFundoX(new Double(0)).comFundoY(new Double(0)).gerarFundosVO();
		
		BarChartModel model = criarPanel();
		ChartSeries linhaX = linhaGrafico();
		ChartSeries linhaY = linhaGrafico();
		linhaX.setLabel("Contribuição X");
		linhaY.setLabel("Contribuição Y");
		
		List<ProjecaoVO> projecaoVOs = new ArrayList<>();
		
		if (validarCampos(calculoPrevidenciaVO)) {
			for (int i = 0; i < calculoPrevidenciaVO.getTempoContribuicao(); i++) {
				fundosVO = recolhimentoAnual(salarioExcedente, fundosVO, reajusteSalarial);
				reajusteSalarial += reajusteAnual(reajusteAnual);
				ProjecaoVO projecaoVO = new ProjecaoVO();
				projecaoVO.setAno(2017 + i);
				projecaoVO.setFundosVO(fundosBuilder.comFundoX(new Double(fundosVO.getFundoX()))
													.comFundoY(new Double(fundosVO.getFundoY()))
													.gerarFundosVO());
				projecaoVOs.add(projecaoVO);
			}
			calculoPrevidenciaVO.setGraficoAnimado(popularGrafico(projecaoVOs, linhaX, linhaY, model));
		}
		
		calculoPrevidenciaVO.getGraficoAnimado().setTitle("Demostrativo");
		calculoPrevidenciaVO.getGraficoAnimado().setAnimate(true);
		calculoPrevidenciaVO.getGraficoAnimado().setLegendPosition("ne");
		calculoPrevidenciaVO.getGraficoAnimado().setShowPointLabels(true);
		calculoPrevidenciaVO.getGraficoAnimado().setLegendCols(2);
		calculoPrevidenciaVO.getGraficoAnimado().setLegendPlacement(LegendPlacement.OUTSIDE);
		calculoPrevidenciaVO.getGraficoAnimado().getAxis(AxisType.Y);
		calculoPrevidenciaVO.getGraficoAnimado().getAxes().put(AxisType.X, new CategoryAxis("Anos"));
		
	}

	public boolean validarCampos(CalculoPrevidenciaVO cp) {
		boolean valido = true;
		valido = validarNome(cp, valido);
		valido = validarContribuicao(cp.getContribuicaoX(), Constantes.CONTRIBUICAO_MINIMA_X,
				Constantes.CONTRIBUICAO_MAXIMA_X, valido, " X no intervalo de 1 à 4");
		valido = validarContribuicao(cp.getContribuicaoY(), Constantes.CONTRIBUICAO_MINIMA_Y,
				Constantes.CONTRIBUICAO_MAXIMA_Y, valido, " Y no intervalo de 0 à 8");
		valido = validarSalario(cp, valido);
		valido = validarTempoContribuicao(cp, valido);
		return valido;
	}

	private boolean validarTempoContribuicao(CalculoPrevidenciaVO cp, boolean valido) {
		if (!(cp.getTempoContribuicao() >= Constantes.TEMPO_MINIMO_CONTRIBUICAO
				&& cp.getTempoContribuicao() <= Constantes.TEMPO_MAXIMO_CONTRIBUICAO)) {
			valido = false;
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Por favor, informe o TEMPO DE CONTRIBUIÇÃO no intervalo entre 2 à 35"));
		}
		return valido;
	}

	private boolean validarSalario(CalculoPrevidenciaVO cp, boolean valido) {
		if (cp.getSalario() == null && cp.getSalario() > 0) {
			valido = false;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Por favor, informe o SALÁRIO"));
		}
		return valido;
	}

	private boolean validarContribuicao(Double contribuicao, Double CONTRIBUICAO_MINIMA, Double CONTRIBUICAO_MAXIMA,
			boolean valido, String cont) {
		if (!(contribuicao >= CONTRIBUICAO_MINIMA && contribuicao <= CONTRIBUICAO_MAXIMA)) {
			valido = false;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Por favor, informe o CONTRIBUIÇÃO" + cont));
		} 
		return valido;
	}

	private boolean validarNome(CalculoPrevidenciaVO calculoPrevidenciaVO, boolean valido) {
		if (StringUtils.isBlank(calculoPrevidenciaVO.getNome())) {
			valido = false;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Por favor, informe o Nome"));
		}
		return valido;
	}

	private FundosVO recolhimentoAnual(Double salarioExcedente, FundosVO fundosVO, Double reajusteSalaria) {
		for (int j = 0; j < 12; j++) {
			fundosVO.setFundoX(fundosVO.getFundoX()
					+ contribuicaoMensal(calculoPrevidenciaVO.getSalario(), calculoPrevidenciaVO.getContribuicaoX()));
			if (reajusteSalaria >= salarioExcedente) {
				fundosVO.setFundoY(fundosVO.getFundoY() + contribuicaoMensal(calculoPrevidenciaVO.getSalario(),
						calculoPrevidenciaVO.getContribuicaoY()));
			}
		}
		return fundosVO;
	}

	public Double contribuicaoMensal(Double salario, Double contribuicao) {
		return salario * (contribuicao / 100);
	}

	private Double reajusteAnual(Double reajusteAnual) {
		Double reajusteSalarial = calculoPrevidenciaVO.getSalario() * (reajusteAnual / 100);
		calculoPrevidenciaVO.setSalario(calculoPrevidenciaVO.getSalario() + reajusteSalarial);
		return reajusteSalarial;
	}
	
	private BarChartModel popularGrafico(List<ProjecaoVO> projecaoVOs, ChartSeries contribuicaoX, 
		ChartSeries contribuicaoY, BarChartModel model) {
        
		projecaoVOs.forEach(p -> {
			contribuicaoX.set(p.getAno(), p.getFundosVO().getFundoX());
			contribuicaoY.set(p.getAno(), p.getFundosVO().getFundoY());
		});
        
        model.addSeries(contribuicaoX);
        model.addSeries(contribuicaoY);
        return model;
    }
	
	public ChartSeries linhaGrafico(){
		return new ChartSeries();
	}
	
	public BarChartModel criarPanel(){
		return new BarChartModel();
	}

}