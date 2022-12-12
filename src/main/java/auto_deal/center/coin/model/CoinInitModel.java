package auto_deal.center.coin.model;

import lombok.Getter;

@Getter
public enum CoinInitModel {

    BTC("BitCoin", "비트코인"),
    ETH("Ethereum", "이더리움"),
    ETC("EthereumClassic", "이더리움클래식"),
    XRP("Ripple", "리플"),
    BCH("BitCoinCash","비트코인캐시");

    private String english;
    private String korea;

    CoinInitModel(String english, String korea) {
        this.english = english;
        this.korea = korea;
    }
}
