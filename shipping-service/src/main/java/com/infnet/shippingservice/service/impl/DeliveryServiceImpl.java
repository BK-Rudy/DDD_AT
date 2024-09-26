package com.infnet.shippingservice.service.impl;

import com.infnet.shippingservice.model.Address;
import com.infnet.shippingservice.model.Delivery;
import com.infnet.shippingservice.model.OrderMessage;
import com.infnet.shippingservice.model.constant.AddressConstant;
import com.infnet.shippingservice.model.enums.OrderStatus;
import com.infnet.shippingservice.rabbitmq.DeliveryProducer;
import com.infnet.shippingservice.repository.DeliveryRepository;
import com.infnet.shippingservice.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryProducer deliveryProducer;

    public void process(OrderMessage orderMessage) {
        log.info("Processando o pedido: {}", orderMessage);

        Delivery delivery = new Delivery();
        delivery.setOrderId(orderMessage.getOrderId());
        delivery.setFreight(ThreadLocalRandom.current().nextFloat(1, 301));
        delivery.setInsuranceId(ThreadLocalRandom.current().nextLong(1, 101));
        delivery.setDriverId(ThreadLocalRandom.current().nextLong(1, 101));
        Address address = address();
        delivery.setAddress(address);
        deliveryRepository.save(delivery);

        log.info("Pedido {} enviado com sucesso!",orderMessage);

        try {
            Thread.sleep(3000);

            log.info("Pedido {} entregue com sucesso!",orderMessage);

            processDelivered(orderMessage);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void processDelivered(OrderMessage orderMessage){
        try {
            orderMessage.setOrderStatus(OrderStatus.DELIVERED);
            deliveryProducer.send(orderMessage);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Address address() {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        Address address = new Address();
        address.setStreetName(AddressConstant.ZIP_CODE[random.nextInt(AddressConstant.ZIP_CODE.length)]);
        address.setStreetName(AddressConstant.STREET[random.nextInt(AddressConstant.STREET.length)]);
        address.setNumber(String.valueOf(random.nextInt(1, 1001)));
        address.setStreetName(AddressConstant.COMPLEMENT[random.nextInt(AddressConstant.COMPLEMENT.length)]);
        address.setStreetName(AddressConstant.NEIGHBORHOOD[random.nextInt(AddressConstant.NEIGHBORHOOD.length)]);
        address.setCity(AddressConstant.CITY[random.nextInt(AddressConstant.CITY.length)]);
        address.setState(AddressConstant.STATE[random.nextInt(AddressConstant.STATE.length)]);

        return address;
    }
}
