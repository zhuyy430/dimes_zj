package com.digitzones.procurement.model;

import com.digitzones.model.EquipmentType;

import javax.persistence.*;

/**
 * 物料装备关联表
 */
@Entity
public class InventoryEquipmentTypeMapping {
    private Long id;

    /**工件*/
    private Inventory inventory;

    /**装备*/
    private EquipmentType equipmentType;

    /**使用频次*/
    private Double useFrequency=1d;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name="INVENTORY_CODE",foreignKey= @ForeignKey(ConstraintMode.NO_CONSTRAINT),referencedColumnName = "cInvCode")
    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @ManyToOne
    @JoinColumn(name="EQUIPMENTTYPE_ID",foreignKey= @ForeignKey(name="none",value=ConstraintMode.NO_CONSTRAINT))
    public EquipmentType getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(EquipmentType equipmentType) {
        this.equipmentType = equipmentType;
    }




    public Double getUseFrequency() {
        return useFrequency;
    }

    public void setUseFrequency(Double useFrequency) {
        this.useFrequency = useFrequency;
    }
}
