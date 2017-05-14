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

}
