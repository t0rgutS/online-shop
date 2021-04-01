package com.devtech.request_response.product;

import com.devtech.request_response.SearchRequest;
import com.devtech.exception.IncorrectOperatorException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@RequiredArgsConstructor
public class ProductSearchRequest extends SearchRequest {
    private String searchString;
    private String priceOp;
    private BigDecimal price;
    private Boolean condition;
    private Boolean shipType;
    private String countOp;
    private Integer count;
    private boolean currentUser = false;

    public void setPriceOp(String priceOp) throws IncorrectOperatorException {
        if (priceOp != null)
            if (!priceOp.isEmpty())
                if (!(priceOp.equals(">") || priceOp.equals("<") || priceOp.equals("=")
                        || priceOp.equals(">=") || priceOp.equals("<=")))
                    throw new IncorrectOperatorException(priceOp);
        this.priceOp = priceOp;
    }

    public void setCountOp(String countOp) throws IncorrectOperatorException {
        if (countOp != null)
            if (!countOp.isEmpty())
                if (!(countOp.equals(">") || countOp.equals("<") || countOp.equals("=")
                        || countOp.equals(">=") || countOp.equals("<=")))
                    throw new IncorrectOperatorException(countOp);
        this.countOp = countOp;
    }
}
