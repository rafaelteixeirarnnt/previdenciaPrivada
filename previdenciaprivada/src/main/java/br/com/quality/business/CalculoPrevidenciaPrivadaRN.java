package br.com.quality.business;

import java.util.ArrayList;
import java.util.List;

import br.com.quality.builder.FundosBuilder;
import br.com.quality.util.Constantes;
import br.com.quality.vo.CalculoPrevidenciaVO;
import br.com.quality.vo.FundosVO;
import br.com.quality.vo.ProjecaoVO;

public class CalculoPrevidenciaPrivadaRN {

	public List<ProjecaoVO> calcular(CalculoPrevidenciaVO calculoPrevidenciaVO) {

		Double reajusteAnual = new Double(Constantes.REAJUSTE_ANUAL);
		Double salarioExcedente = new Double(calculoPrevidenciaVO.getSalario() * (50.0 / 100));
		Double reajusteSalarial = new Double(0);
		
		FundosBuilder fundosBuilder = new FundosBuilder();
		FundosVO fundosVO = fundosBuilder.comFundoX(new Double(0)).comFundoY(new Double(0)).gerarFundosVO();

		List<ProjecaoVO> projecaoVOs = new ArrayList<>();

		for (int i = 0; i < calculoPrevidenciaVO.getTempoContribuicao(); i++) {
			fundosVO = recolhimentoAnual(salarioExcedente, fundosVO, reajusteSalarial, calculoPrevidenciaVO);
			reajusteSalarial += reajusteAnual(reajusteAnual, calculoPrevidenciaVO);
			ProjecaoVO projecaoVO = new ProjecaoVO();
			projecaoVO.setAno(2017 + i);
			projecaoVO.setFundosVO(fundosBuilder.comFundoX(new Double(fundosVO.getFundoX()))
					.comFundoY(new Double(fundosVO.getFundoY())).gerarFundosVO());
			projecaoVOs.add(projecaoVO);
		}

		return projecaoVOs;
	}

	private FundosVO recolhimentoAnual(Double salarioExcedente, FundosVO fundosVO, Double reajusteSalaria,
			CalculoPrevidenciaVO cp) {
		for (int j = 0; j < 12; j++) {
			fundosVO.setFundoX(fundosVO.getFundoX() + contribuicaoMensal(cp.getSalario(), cp.getContribuicaoX()));
			if (reajusteSalaria >= salarioExcedente) {
				fundosVO.setFundoY(fundosVO.getFundoY() + contribuicaoMensal(cp.getSalario(), cp.getContribuicaoY()));
			}
		}
		return fundosVO;
	}

	public Double contribuicaoMensal(Double salario, Double contribuicao) {
		return salario * (contribuicao / 100);
	}

	private Double reajusteAnual(Double reajusteAnual, CalculoPrevidenciaVO cp) {
		Double reajusteSalarial = cp.getSalario() * (reajusteAnual / 100);
		cp.setSalario(cp.getSalario() + reajusteSalarial);
		return reajusteSalarial;
	}

}