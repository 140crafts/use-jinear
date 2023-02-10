package co.jinear.core.model.entity;

import co.jinear.core.model.enumtype.localestring.LocaleStringType;
import co.jinear.core.model.enumtype.localestring.LocaleType;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "locale_string")
public class LocaleString extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "locale_string_id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "string_type")
    private LocaleStringType stringType;

    @Enumerated(EnumType.STRING)
    @Column(name = "locale")
    private LocaleType locale;

    @Column(name = "value")
    private String value;
}
