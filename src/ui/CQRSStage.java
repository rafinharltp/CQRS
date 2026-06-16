package ui;

public enum CQRSStage {

    NONE,

    INPUT,

    COMMAND,
    COMMAND_HANDLER,

    QUERY,
    QUERY_HANDLER,

    REPOSITORY
}