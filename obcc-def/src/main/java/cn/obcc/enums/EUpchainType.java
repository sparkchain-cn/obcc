package cn.obcc.enums;

public enum EUpchainType {
    Transfer("Transfer", "Transfer"),
    ContractDeploy("Deploy", "Deploy"),
    ContractInvoke("Invoke", "Invoke"),
    ContractCompile("Compile", "Compile"),
    BlockBack("BlockBack", "BlockBack");
    private String name;
    private String descr;

    private EUpchainType(String name, String desc) {
        this.name = name;
        this.descr = desc;
    }
}
