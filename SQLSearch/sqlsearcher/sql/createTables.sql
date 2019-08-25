CREATE TABLE alpha (
    COL	varchar(40),
    DES	varchar(200),
    MIN	int unsigned,
    AV	numeric(5,2),
    MAX	int unsigned,
    FIL	varchar(50)
) CHARACTER SET utf8;

CREATE TABLE alphad (
    COL	varchar(40),
    DES	varchar(200),
    MIN	int unsigned,
    AV	numeric(5,2),
    MAX	int unsigned,
    FIL	varchar(50)
) CHARACTER SET utf8;


CREATE TABLE alpha2 (
    COL	varchar(40),
    DES	varchar(200),
    MIN	int unsigned,
    AV	numeric(5,2),
    MAX	int unsigned,
    FIL	varchar(50)
) CHARACTER SET utf8;


CREATE TABLE alpha2d (
    COL	varchar(40),
    DES	varchar(200),
    MIN	int unsigned,
    AV	numeric(5,2),
    MAX	int unsigned,
    FIL	varchar(50)
) CHARACTER SET utf8;


CREATE TABLE alpha3 (
    COL	varchar(40),
    DES	varchar(200),
    MIN	int unsigned,
    AV	numeric(5,2),
    MAX	int unsigned,
    FIL	varchar(50)
) CHARACTER SET utf8;


CREATE TABLE alpha3d (
    COL	varchar(40),
    DES	varchar(200),
    MIN	int unsigned,
    AV	numeric(5,2),
    MAX	int unsigned,
    FIL	varchar(50)
) CHARACTER SET utf8;



CREATE TABLE bravo (
    CUI1	char(8) NOT NULL,
    AUI1	varchar(9),
    STYPE1	varchar(50) NOT NULL,
    REL	varchar(4) NOT NULL,
    CUI2	char(8) NOT NULL,
    AUI2	varchar(9),
    STYPE2	varchar(50) NOT NULL,
    RUI	varchar(10) NOT NULL
) CHARACTER SET utf8;


CREATE TABLE bravod (
    CUI1	char(8) NOT NULL,
    RUI	varchar(10) NOT NULL
) CHARACTER SET utf8;


CREATE TABLE bravo2 (
    CUI1	char(8) NOT NULL,
    AUI1	varchar(9),
    STYPE1	varchar(50) NOT NULL,
    REL	varchar(4) NOT NULL,
    CUI2	char(8) NOT NULL,
    AUI2	varchar(9),
    STYPE2	varchar(50) NOT NULL,
    RUI	varchar(10) NOT NULL
) CHARACTER SET utf8;


CREATE TABLE bravo2d (
    CUI1	char(8) NOT NULL,
    RUI	varchar(10) NOT NULL
) CHARACTER SET utf8;




CREATE TABLE charlie (
    RSAB	varchar(40) NOT NULL,
    SF	varchar(40) NOT NULL,
    TFR	int unsigned,
    CFR	int unsigned,
    TTYL	varchar(400)
) CHARACTER SET utf8;


CREATE TABLE charlied (
    RSAB	varchar(40) NOT NULL,
    SF	varchar(40) NOT NULL,
    TFR	int unsigned,
    CFR	int unsigned,
    TTYL	varchar(400)
) CHARACTER SET utf8;


CREATE TABLE charlie2 (
    RSAB	varchar(40) NOT NULL,
    SF	varchar(40) NOT NULL,
    TFR	int unsigned,
    CFR	int unsigned,
    TTYL	varchar(400)
) CHARACTER SET utf8;

CREATE TABLE charlie2d (
    RSAB	varchar(40) NOT NULL,
    SF	varchar(40) NOT NULL,
    TFR	int unsigned,
    CFR	int unsigned,
    TTYL	varchar(400)
) CHARACTER SET utf8;

CREATE TABLE charlie3 (
    RSAB	varchar(40) NOT NULL,
    SF	varchar(40) NOT NULL,
    TFR	int unsigned,
    CFR	int unsigned,
    TTYL	varchar(400)
) CHARACTER SET utf8;

CREATE TABLE charlie3d (
    RSAB	varchar(40) NOT NULL,
    SF	varchar(40) NOT NULL,
    TFR	int unsigned,
    CFR	int unsigned,
    TTYL	varchar(400)
) CHARACTER SET utf8;




CREATE TABLE delta (
    RSAB	varchar(40) NOT NULL,
    SF	varchar(40) NOT NULL,
    TFR	int unsigned,
    CFR	int unsigned,
    TTYL	varchar(400)
) CHARACTER SET utf8;

CREATE TABLE deltad (
    RSAB	varchar(40) NOT NULL,
    TFR	int unsigned
) CHARACTER SET utf8;



CREATE TABLE echo (
    MRRANK_RANK	int unsigned NOT NULL,
    SAB	varchar(40) NOT NULL,
    TTY	varchar(40) NOT NULL,
    SUPPRESS	char(1) NOT NULL
) CHARACTER SET utf8;


CREATE TABLE echod (
    MRRANK_RANK	int unsigned NOT NULL,
    SAB	varchar(40) NOT NULL,
    TTY	varchar(40) NOT NULL,
    SUPPRESS	char(1) NOT NULL
) CHARACTER SET utf8;



CREATE TABLE echo2 (
    MRRANK_RANK	int unsigned NOT NULL,
    SAB	varchar(40) NOT NULL,
    TTY	varchar(40) NOT NULL,
    SUPPRESS	char(1) NOT NULL
) CHARACTER SET utf8;


CREATE TABLE echo2d (
    MRRANK_RANK	int unsigned NOT NULL,
    SAB	varchar(40) NOT NULL,
    TTY	varchar(40) NOT NULL,
    SUPPRESS	char(1) NOT NULL
) CHARACTER SET utf8;


CREATE TABLE foxtrot (
    MRRANK_RANK	int unsigned NOT NULL,
    SAB	varchar(40) NOT NULL,
    TTY	varchar(40) NOT NULL,
    SUPPRESS	char(1) NOT NULL
) CHARACTER SET utf8;

CREATE TABLE foxtrotd (
    MRRANK_RANK	int unsigned NOT NULL,
    SAB	varchar(40) NOT NULL,
    TTY	varchar(40) NOT NULL,
    SUPPRESS	char(1) NOT NULL
) CHARACTER SET utf8;

CREATE TABLE foxtrot2 (
    MRRANK_RANK	int unsigned NOT NULL,
    SAB	varchar(40) NOT NULL,
    TTY	varchar(40) NOT NULL,
    SUPPRESS	char(1) NOT NULL
) CHARACTER SET utf8;

CREATE TABLE foxtrot2d (
    MRRANK_RANK	int unsigned NOT NULL,
    SAB	varchar(40) NOT NULL,
    TTY	varchar(40) NOT NULL,
    SUPPRESS	char(1) NOT NULL
) CHARACTER SET utf8;

CREATE TABLE golf (
    VCUI	char(8),
    RCUI	char(8),
    VSAB	varchar(40) NOT NULL,
    RSAB	varchar(40) NOT NULL,
    SON	text NOT NULL,
    SF	varchar(40) NOT NULL,
    SVER	varchar(40)
) CHARACTER SET utf8;

CREATE TABLE golfd (
    SVER	varchar(40)
) CHARACTER SET utf8;

CREATE TABLE golf2 (
    VCUI	char(8),
    RCUI	char(8),
    VSAB	varchar(40) NOT NULL,
    RSAB	varchar(40) NOT NULL,
    SON	text NOT NULL,
    SF	varchar(40) NOT NULL,
    SVER	varchar(40)
) CHARACTER SET utf8;

CREATE TABLE golf2d (
    SVER	varchar(40)
) CHARACTER SET utf8;


CREATE TABLE hotel (
    CUI	char(8) NOT NULL,
    TUI	char(4) NOT NULL,
    STN	varchar(100) NOT NULL
) CHARACTER SET utf8;

CREATE TABLE hoteld (
    CUI	char(8) NOT NULL,
    TUI	char(4) NOT NULL,
    STN	varchar(100) NOT NULL
) CHARACTER SET utf8;

CREATE TABLE hotel2 (
    CUI	char(8) NOT NULL,
    TUI	char(4) NOT NULL,
    STN	varchar(100) NOT NULL
) CHARACTER SET utf8;

CREATE TABLE hotel2d (
    CUI	char(8) NOT NULL,
    TUI	char(4) NOT NULL,
    STN	varchar(100) NOT NULL
) CHARACTER SET utf8;




CREATE TABLE india (
    CUI	char(8) NOT NULL,
    TUI	char(4) NOT NULL,
    CVF	int unsigned
) CHARACTER SET utf8;

CREATE TABLE juliett (
    CUI	char(8) NOT NULL,
    LAT	char(3) NOT NULL,
    TS	char(1) NOT NULL,
    LUI	varchar(10) NOT NULL,
    STT	varchar(3) NOT NULL,
    SUI	varchar(10) NOT NULL,
    ISPREF	char(1) NOT NULL
) CHARACTER SET utf8;


CREATE TABLE indiad (
    LAT	char(3) NOT NULL,
    STT	varchar(3) NOT NULL,
    ISPREF	char(1) NOT NULL
) CHARACTER SET utf8;





CREATE TABLE kilo (
    LUI	varchar(10) NOT NULL,
    CUI	char(8) NOT NULL
) CHARACTER SET utf8;

CREATE TABLE kilo2 (
    LUI	varchar(10) NOT NULL,
    CUI	char(8) NOT NULL
) CHARACTER SET utf8;

CREATE TABLE kilod (
    LUI	varchar(10) NOT NULL,
    CUI	char(8) NOT NULL
) CHARACTER SET utf8;

CREATE TABLE kilo2d (
    LUI	varchar(10) NOT NULL,
    CUI	char(8) NOT NULL
) CHARACTER SET utf8;