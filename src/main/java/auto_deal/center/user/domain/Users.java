package auto_deal.center.user.domain;

import auto_deal.center.quant.domain.Quant;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "TB_USER")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="USER_ID")
    private Long id;

    private Long chatId;

    private LocalDateTime alarmTime;

    private LocalDateTime regDate;

    @OneToMany(mappedBy = "users")
    @Builder.Default
    private List<Quant> quants = new ArrayList<>();

    public void addQuant(Quant quant){
        this.quants.add(quant);
        
        // 무한루프에 빠지지 않도록 체크
        if(quant.getUsers() != this){
            quant.setUsers(this);
        }
    }
}
