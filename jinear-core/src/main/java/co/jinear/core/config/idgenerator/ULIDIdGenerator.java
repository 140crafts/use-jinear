package co.jinear.core.config.idgenerator;

import de.huxhorn.sulky.ulid.ULID;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.Locale;

public class ULIDIdGenerator implements IdentifierGenerator {

    private static final ULID ulid = new ULID();

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        return ulid.nextULID().toLowerCase(Locale.ROOT);
    }
}
