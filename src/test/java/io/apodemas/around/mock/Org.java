package io.apodemas.around.mock;

/**
 * @author: Cao Zheng
 * @date: 2023/5/27
 * @description:
 */
public class Org {
    private long id;
    private String name;
    private Long creatorId;
    private String creatorName;
    private Long operatorId;
    private String operatorName;

    public Org(long id, String name, Long creatorId, Long operatorId) {
        this.id = id;
        this.name = name;
        this.creatorId = creatorId;
        this.operatorId = operatorId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }
}
