package br.com.quality.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FundosVO {

	private Double fundoX;

	private Double fundoY;

	public FundosVO(Double fundoX, Double fundoY) {
		this.fundoX = fundoX;
		this.fundoY = fundoY;
	}

	public Double getFundoX() {
		return fundoX;
	}

	public void setFundoX(Double fundoX) {
		this.fundoX = fundoX;
	}

	public Double getFundoY() {
		return fundoY;
	}

	public void setFundoY(Double fundoY) {
		this.fundoY = fundoY;
	}

}
