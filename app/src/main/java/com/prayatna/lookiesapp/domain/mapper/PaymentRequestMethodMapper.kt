import com.prayatna.lookiesapp.domain.model.transaction.PaymentRequestMethod
import com.prayatna.lookiesapp.presentation.transaction.payment.state.PaymentMethod

fun PaymentMethod.toDomain(): PaymentRequestMethod {
    return when (this) {
        PaymentMethod.GOPAY -> PaymentRequestMethod.GOPAY
        PaymentMethod.CREDIT_CARD -> PaymentRequestMethod.CREDIT_CARD
    }
}