CREATE TABLE Category
(
    CID VARCHAR2(10) NOT NULL,
    Name VARCHAR2(255) NOT NULL,
    Is_Main NUMBER(1),
    CONSTRAINT PK_Category PRIMARY KEY(CID),
    CONSTRAINT UNQ_Category UNIQUE(Name)
);

CREATE TABLE Category_Relation
(
    Main_Cid VARCHAR2(10) NOT NULL,
    Sub_Cid VARCHAR2(10) NOT NULL,
    CONSTRAINT PK_Category_Relation PRIMARY KEY(Main_Cid, Sub_Cid),
    CONSTRAINT FK_PK_Category_Relation_Main FOREIGN KEY(Main_Cid)
        REFERENCES Category(CID)
        ON DELETE CASCADE,
    CONSTRAINT FK_PK_Category_Relation_Sub FOREIGN KEY(Sub_Cid)
        REFERENCES Category(CID)
        ON DELETE CASCADE
);

CREATE TABLE Business
(
    BID VARCHAR2(10) NOT NULL,
    Business_Uid VARCHAR2(22) NOT NULL,
    Name VARCHAR2(255),
    Address VARCHAR2(255),
    City VARCHAR2(255),
    State VARCHAR2(255),
    Stars VARCHAR2(255),
    Review_Count INTEGER,
    Is_Open NUMBER(1),
    CONSTRAINT PK_Business PRIMARY KEY(BID),
    CONSTRAINT UNQ_Business UNIQUE(Business_Uid)
);

CREATE TABLE Business_Hours
(
    BID VARCHAR2(10) NOT NULL,
    Day VARCHAR2(9) NOT NULL,
    Open_Time INTEGER NOT NULL,
    Close_Time INTEGER NOT NULL,
    CONSTRAINT PK_Business_Hours PRIMARY KEY(BID, Day, Open_Time, Close_Time),
    CONSTRAINT FK_Business_Hours_Business FOREIGN KEY(BID)
        REFERENCES Business(BID)
        ON DELETE CASCADE
);

CREATE TABLE Business_Category
(
    BID VARCHAR2(10) NOT NULL,
    Category_Name VARCHAR2(255) NOT NULL,
    CONSTRAINT PK_Business_Category PRIMARY KEY(BID, Category_Name),
    CONSTRAINT FK_Business_Category_Business FOREIGN KEY(BID)
        REFERENCES Business(BID)
        ON DELETE CASCADE,
    CONSTRAINT FK_Business_Category_Category FOREIGN KEY(Category_Name)
        REFERENCES Category(Name)
        ON DELETE CASCADE
);

CREATE TABLE Business_Attribute
(
    BID VARCHAR2(10) NOT NULL,
    Name VARCHAR2(255) NOT NULL,
    CONSTRAINT PK_Business_Attribute PRIMARY KEY(BID, Name),
    CONSTRAINT FK_Business_Attribute_Business FOREIGN KEY(BID)
        REFERENCES Business(BID)
        ON DELETE CASCADE
);

CREATE TABLE Yelp_User
(
    "UID" VARCHAR2(10) NOT NULL,
    User_Uid VARCHAR2(22) NOT NULL,
    Name VARCHAR2(255),
    CONSTRAINT PK_Yelp_User PRIMARY KEY("UID"),
    CONSTRAINT UNQ_Yelp_User UNIQUE(User_Uid)
);

CREATE TABLE Review
(
    RID VARCHAR2(10) NOT NULL,
    Review_Uid VARCHAR2(22) NOT NULL,
    User_Uid VARCHAR2(22) NOT NULL,
    Business_Uid VARCHAR2(22) NOT NULL,
    Stars INTEGER,
    Review_Date DATE,
    Funny_Votes INTEGER,
    Cool_Votes INTEGER,
    Useful_Votes INTEGER,
    Text CLOB,
    CONSTRAINT PK_Review PRIMARY KEY(RID),
    CONSTRAINT FK_Review_User FOREIGN KEY(User_Uid)
        REFERENCES Yelp_User(User_Uid)
        ON DELETE CASCADE,
    CONSTRAINT FK_Review_Business FOREIGN KEY(Business_Uid)
        REFERENCES Business(Business_Uid)
        ON DELETE CASCADE,
    CONSTRAINT UNQ_Review UNIQUE(Review_Uid)
);

CREATE TABLE Checkin
(
    Cid VARCHAR2(10) NOT NULL,
    Business_Uid VARCHAR2(22) NOT NULL,
    Day INTEGER,
    Hour INTEGER,
    Count INTEGER,
    CONSTRAINT PK_Checkin PRIMARY KEY(Cid),
    CONSTRAINT FK_Checkin_Business FOREIGN KEY(Business_Uid)
        REFERENCES Business(Business_Uid)
        ON DELETE CASCADE
);

CREATE INDEX IDX_Category_Is_Main ON Category(Is_Main);
CREATE INDEX IDX_Business_City ON Business(City);
CREATE INDEX IDX_Business_State ON Business(State);
CREATE INDEX IDX_Review_User ON Review(User_Uid);
CREATE INDEX IDX_Review_Business ON Review(Business_Uid);
CREATE INDEX IDX_Checkin_Business ON Checkin(Business_Uid);