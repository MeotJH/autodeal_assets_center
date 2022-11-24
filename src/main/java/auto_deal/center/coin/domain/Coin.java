package auto_deal.center.coin.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Table(name="TB_COIN")
public class Coin {

    @Id
    @GeneratedValue
    private Long id;
    private String kor_name;

    private String eng_name;

    private String ticker;

    public void setCoinOne(String eng_name, String ticker){
        this.eng_name = eng_name;
        this.ticker = ticker;
    }

}
