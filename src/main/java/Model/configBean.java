package Model;

public class configBean {

    private String sheetNameInput;
    private String sourceFileName;
    private int templateFileName;
    private int fillFileName;
    private int fillDestFileName;
    private int fillDestFileNameOnlyNameAndPhone;
    private int fillDestFileNameNoMsg;
    public void setSheetNameInput(String sheetNameInput) {
        this.sheetNameInput = sheetNameInput;
    }
    public String getSheetNameInput() {
        return sheetNameInput;
    }

    public void setSourceFileName(String sourceFileName) {
        this.sourceFileName = sourceFileName;
    }
    public String getSourceFileName() {
        return sourceFileName;
    }

    public void setTemplateFileName(int templateFileName) {
        this.templateFileName = templateFileName;
    }
    public int getTemplateFileName() {
        return templateFileName;
    }

    public void setFillFileName(int fillFileName) {
        this.fillFileName = fillFileName;
    }
    public int getFillFileName() {
        return fillFileName;
    }

    public void setFillDestFileName(int fillDestFileName) {
        this.fillDestFileName = fillDestFileName;
    }
    public int getFillDestFileName() {
        return fillDestFileName;
    }

    public void setFillDestFileNameOnlyNameAndPhone(int fillDestFileNameOnlyNameAndPhone) {
        this.fillDestFileNameOnlyNameAndPhone = fillDestFileNameOnlyNameAndPhone;
    }
    public int getFillDestFileNameOnlyNameAndPhone() {
        return fillDestFileNameOnlyNameAndPhone;
    }

    public void setFillDestFileNameNoMsg(int fillDestFileNameNoMsg) {
        this.fillDestFileNameNoMsg = fillDestFileNameNoMsg;
    }
    public int getFillDestFileNameNoMsg() {
        return fillDestFileNameNoMsg;
    }

}
