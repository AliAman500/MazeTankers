package components;

import ECS.Component;
import ECS.ESystem;
import ECS.Entity;

public class NetworkTank extends Component {
    private Tank tank;

    public NetworkTank(Entity parent) {
        super(parent);
        tank = (Tank) parent.getComponent("Tank");
    }

    public void update(ESystem eSystem) {
        // control tank:
    }
}
