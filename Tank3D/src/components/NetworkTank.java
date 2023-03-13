package components;

import ECS.Component;
import ECS.ESystem;
import ECS.Entity;

public class NetworkTank extends Component {
    private Tank tank;
    private GunRecoil recoil;

    public NetworkTank(Entity parent) {
        super(parent);
        tank = (Tank) parent.getComponent("Tank");
        recoil = (GunRecoil) parent.getComponent("GunRecoil");
    }

    public void update(ESystem eSystem) {
        // control tank:
    }
}
