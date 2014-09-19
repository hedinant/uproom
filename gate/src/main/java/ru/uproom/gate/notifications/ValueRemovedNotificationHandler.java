package ru.uproom.gate.notifications;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zwave4j.Notification;
import org.zwave4j.NotificationType;
import ru.uproom.gate.transport.ServerTransportMarker;
import ru.uproom.gate.transport.dto.parameters.DeviceParametersNames;
import ru.uproom.gate.transport.dto.parameters.ZWaveParamId2ServerParamId;
import ru.uproom.gate.zwave.ZWaveHome;
import ru.uproom.gate.zwave.ZWaveNode;
import ru.uproom.gate.zwave.ZWaveValue;
import ru.uproom.gate.zwave.ZWaveValueIndexFactory;

/**
 * Created by osipenko on 15.09.14.
 */

@ZwaveNotificationHandlerAnnotation(value = NotificationType.VALUE_REMOVED)
public class ValueRemovedNotificationHandler implements NotificationHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ValueRemovedNotificationHandler.class);

    @Override
    public boolean execute(ZWaveHome home, ServerTransportMarker transport, Notification notification) {

        // находим узел из которого удаляется параметр
        ZWaveNode node = home.getNodes().get(notification.getNodeId());
        if (node == null) return false;

        // удаляем параметр
        Integer index = ZWaveValueIndexFactory.createIndex(notification.getValueId());
        DeviceParametersNames name = ZWaveParamId2ServerParamId.getServerParamId(index);
        ZWaveValue value = (ZWaveValue) node.getParams().remove(name);

        // todo : send message to server about this, maybe

        LOG.debug("z-wave notification : VALUE_REMOVED; node ID : {}; value label : {}",
                node.getZId(),
                value.getValueLabel()
        );
        return true;
    }
}
