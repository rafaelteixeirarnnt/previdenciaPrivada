package br.com.quality.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LegendPlacement;

import br.com.quality.builder.CalculoPrevidenciaBuilder;
import br.com.quality.business.CalculoPrevidenciaPrivadaRN;
import br.com.quality.util.Constantes;
import br.com.quality.vo.CalculoPrevidenciaVO;
import br.com.quality.vo.FundosVO;
import br.com.quality.vo.ProjecaoVO;
import lombok.Getter;
import lombok.Setter;

@Named
@RequestScoped
public class CalculoPrevidenciaPrivadaController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private CalculoPrevidenciaVO calculoPrevidenciaVO;

	@Getter
	@Setter
	private BarChartModel grafico = new BarChartModel();

	@Inject
	private CalculoPrevidenciaPrivadaRN calculoPrevidenciaPrivadaRN;

	@Getter
	@Setter
	private FundosVO fundosVO;

	@PostConstruct
	public void inicializar() {
		CalculoPrevidenciaBuilder builder = new CalculoPrevidenciaBuilder();
		calculoPrevidenciaVO = builder.comNome("").comContribuicaoX(new Double(0)).comContribuicaoY(new Double(0))
				.comSalario(new Double(0)).comTempoContribuicao(new Integer(2)).comGraficoAnimado(new BarChartModel())
				.gerarCalculoPrevidenciaVO();
	}

	public void calcular() {
		if (validarCampos(calculoPrevidenciaVO)) {
			List<ProjecaoVO> projecaoVOs = calculoPrevidenciaPrivadaRN.calcular(calculoPrevidenciaVO);
			apresentarGrafico(projecaoVOs);
			ProjecaoVO projecaoVO = projecaoVOs.get(projecaoVOs.size() - 1);
			setFundosVO(projecaoVO.getFundosVO());
			inicializar();
		}
	}

	private void apresentarGrafico(List<ProjecaoVO> projecaoVOs) {
		BarChartModel model = new BarChartModel();
		ChartSeries linhaX = new ChartSeries();
		ChartSeries linhaY = new ChartSeries();

		linhaX.setLabel("Contribui\u00e7\u00e3o X");
		linhaY.setLabel("Contribui\u00e7\u00e3o Y");
		calculoPrevidenciaVO.setGraficoAnimado(popularGrafico(projecaoVOs, linhaX, linhaY, model));
		grafico = calculoPrevidenciaVO.getGraficoAnimado();

		calculoPrevidenciaVO.getGraficoAnimado().setTitle("Demostrativo");
		calculoPrevidenciaVO.getGraficoAnimado().setAnimate(true);
		calculoPrevidenciaVO.getGraficoAnimado().setLegendPosition("ne");
		calculoPrevidenciaVO.getGraficoAnimado().setShowPointLabels(true);
		calculoPrevidenciaVO.getGraficoAnimado().setLegendPlacement(LegendPlacement.OUTSIDE);
		calculoPrevidenciaVO.getGraficoAnimado().getAxis(AxisType.Y);
		calculoPrevidenciaVO.getGraficoAnimado().getAxes().put(AxisType.X, new CategoryAxis("Anos"));
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

	public boolean validarCampos(CalculoPrevidenciaVO cp) {
		boolean valido = true;
		valido = validarContribuicao(cp.getContribuicaoX(), Constantes.CONTRIBUICAO_MINIMA_X,
				Constantes.CONTRIBUICAO_MAXIMA_X, valido, " X no intervalo de 1 \u00e0 4");
		valido = validarContribuicao(cp.getContribuicaoY(), Constantes.CONTRIBUICAO_MINIMA_Y,
				Constantes.CONTRIBUICAO_MAXIMA_Y, valido, " Y no intervalo de 0 \u00e0 8");
		valido = validarSalario(cp, valido);
		valido = validarTempoContribuicao(cp, valido);
		return valido;
	}

	private boolean validarTempoContribuicao(CalculoPrevidenciaVO cp, boolean valido) {
		if (!(cp.getTempoContribuicao() >= Constantes.TEMPO_MINIMO_CONTRIBUICAO
				&& cp.getTempoContribuicao() <= Constantes.TEMPO_MAXIMO_CONTRIBUICAO)) {
			valido = false;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
					"Por favor, informe o TEMPO DE CONTRIBUI\u00c7\u00c3O no intervalo entre 2 \u00e0 35"));
		}
		return valido;
	}

	private boolean validarSalario(CalculoPrevidenciaVO cp, boolean valido) {
		if (cp.getSalario() == null && cp.getSalario() > 0) {
			valido = false;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Por favor, informe o SAL\u00c1RIO"));
		}
		return valido;
	}

	private boolean validarContribuicao(Double contribuicao, Double CONTRIBUICAO_MINIMA, Double CONTRIBUICAO_MAXIMA,
			boolean valido, String cont) {
		if (!(contribuicao >= CONTRIBUICAO_MINIMA && contribuicao <= CONTRIBUICAO_MAXIMA)) {
			valido = false;

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Por favor, informe a CONTRIBUI\u00c7\u00c3O" + cont));
		}
		return valido;
	}

	public CalculoPrevidenciaVO getCalculoPrevidenciaVO() {
		return calculoPrevidenciaVO;
	}

	public void setCalculoPrevidenciaVO(CalculoPrevidenciaVO calculoPrevidenciaVO) {
		this.calculoPrevidenciaVO = calculoPrevidenciaVO;
	}

	public BarChartModel getGrafico() {
		return grafico;
	}

	public void setGrafico(BarChartModel grafico) {
		this.grafico = grafico;
	}

	public FundosVO getFundosVO() {
		return fundosVO;
	}

	public void setFundosVO(FundosVO fundosVO) {
		this.fundosVO = fundosVO;
	}

}