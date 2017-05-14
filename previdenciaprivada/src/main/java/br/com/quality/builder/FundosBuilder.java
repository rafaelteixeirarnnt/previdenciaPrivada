package br.com.quality.builder;

import br.com.quality.vo.FundosVO;

public class FundosBuilder {
	
	private Double fundoX;
	
	private Double fundoY;
	
	public FundosBuilder comFundoX(Double fundoX){
		this.fundoX = fundoX;
		return this;
	}
	
	public FundosBuilder comFundoY(Double fundoY){
		this.fundoY = fundoY;
		return this;
	}
	
	public FundosVO gerarFundosVO(){
		return new FundosVO(fundoX, fundoY);
	}

}
