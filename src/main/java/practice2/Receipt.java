package practice2;

import java.math.BigDecimal;
import java.util.List;

public class Receipt {

    public Receipt() {
        tax = transDoubleToDecimal(0.1);
    }

    private BigDecimal tax;

    public double CalculateGrandTotal(List<Product> products, List<OrderItem> items) {
        BigDecimal subTotal = calculateSubtotal(products, items);

        subTotal = SubtractDiscounts(products, items, subTotal);

        BigDecimal grandTotal = addTax(subTotal);

        return transDecimalToDouble(grandTotal);
    }

    private OrderItem findOrderItemByProduct(List<OrderItem> items, Product product) {
        OrderItem curItem = null;
        for (OrderItem item : items) {
            if (item.getCode() == product.getCode()) {
                curItem = item;
                break;
            }
        }
        return curItem;
    }

    private BigDecimal calculateSubtotal(List<Product> products, List<OrderItem> items) {
        BigDecimal subTotal = new BigDecimal(0);
        for (Product product : products) {
            OrderItem item = findOrderItemByProduct(items, product);
            BigDecimal itemTotal = product.getPrice().multiply(new BigDecimal(item.getCount()));
            subTotal = subTotal.add(itemTotal);
        }
        return subTotal;
    }

    private BigDecimal transDoubleToDecimal(double doubleNum) {
        BigDecimal decimal = new BigDecimal(doubleNum);
        return decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private double transDecimalToDouble(BigDecimal decimal) {
        return decimal.doubleValue();
    }


    /**
     * 加上税钱
     * @param subTotal
     * @return
     */
    private BigDecimal addTax(BigDecimal subTotal) {
        BigDecimal taxTotal = subTotal.multiply(tax);
        return subTotal.add(taxTotal);
    }

    /**
     * 减去折扣
     * @param products
     * @param items
     * @param subTotal
     * @return
     */
    private BigDecimal SubtractDiscounts(List<Product> products, List<OrderItem> items, BigDecimal subTotal) {
        for (Product product : products) {
            OrderItem curItem = findOrderItemByProduct(items, product);

            BigDecimal reducedPrice = product.getPrice()
                    .multiply(product.getDiscountRate())
                    .multiply(new BigDecimal(curItem.getCount()));

            subTotal = subTotal.subtract(reducedPrice);
        }
        return subTotal;
    }
}
