package com.trinity.trinity.domain.control.dto.response;

import com.trinity.trinity.domain.logic.dto.GameRoom;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommonDataDto {
    private boolean fertilizerUpgrade;
    private boolean barrierUpgrade;
    private boolean conflictAsteroid;

    @Builder
    public CommonDataDto(boolean fertilizerUpgrade, boolean barrierUpgrade, boolean conflictAsteroid) {
        this.fertilizerUpgrade = fertilizerUpgrade;
        this.barrierUpgrade = barrierUpgrade;
        this.conflictAsteroid = conflictAsteroid;
    }

    public void setCommonDto(GameRoom gameroom) {
        if (gameroom.getFirstRoom().getFertilizerUpgradeStatus() == 3) {
            this.fertilizerUpgrade = true;
        } else {
            this.fertilizerUpgrade = false;
        }

        if (gameroom.getThirdRoom().getBarrierStatus() == 2) {
            this.barrierUpgrade = true;
        } else {
            this.barrierUpgrade = false;
        }
    }
}
