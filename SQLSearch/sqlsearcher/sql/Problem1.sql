-- Problem 1 --

-- SOLUTION: SELECT * FROM alpha WHERE MIN=0

-- SIMPLE SELECT --

-- SETUP






INSERT INTO alpha (COL,DES,MIN,AV,MAX,FIL)
values ('ATNL', 'Attribute name list for a source.', '0', '28.30', '1050', 'MRSAB.RRF');

INSERT INTO alpha (COL,DES,MIN,AV,MAX,FIL)
values ('AUI1', 'Unique identifier for first atom', '0', '8.52', '9', 'MRREL.RRF');

INSERT INTO alpha (COL,DES,MIN,AV,MAX,FIL)
values ('AUI2', 'Unique identifier for second atom', '0', '8.52', '9', 'MRREL.RRF');

INSERT INTO alpha (COL,DES,MIN,AV,MAX,FIL)
values ('ATN', 'Attribute name', '2', '5.44', '62', 'MRSAT.RRF');

INSERT INTO alpha (COL,DES,MIN,AV,MAX,FIL)
values ('ATUI', 'Unique identifier for attribute.', '10', '10.66', '11', 'MRSTY.RRF');

INSERT INTO alpha (COL,DES,MIN,AV,MAX,FIL)
values ('ATUI', 'Unique identifier for attribute.', '10', '10.77', '11', 'MRSAT.RRF');

INSERT INTO alpha (COL,DES,MIN,AV,MAX,FIL)
values ('ATUI', 'Unique identifier for attribute.', '10', '10.83', '11', 'MRDEF.RRF');

INSERT INTO alpha (COL,DES,MIN,AV,MAX,FIL)
values ('ATV', 'Attribute value', '1', '11.65', '3997', 'MRSAT.RRF');

INSERT INTO alpha (COL,DES,MIN,AV,MAX,FIL)
values ('AUI1', 'Unique identifier for first atom', '8', '8.52', '9', 'MRAUI.RRF');

INSERT INTO alpha (COL,DES,MIN,AV,MAX,FIL)
values ('AUI2', 'Unique identifier for second atom', '8', '8.52', '9', 'MRAUI.RRF');

INSERT INTO alpha (COL,DES,MIN,AV,MAX,FIL)
values ('AUI', 'Unique identifier for atom', '8', '8.75', '9', 'MRDEF.RRF');

INSERT INTO alpha (COL,DES,MIN,AV,MAX,FIL)
values ('AUI', 'Unique identifier for atom', '8', '8.78', '9', 'MRHIER.RRF');

INSERT INTO alpha (COL,DES,MIN,AV,MAX,FIL)
values ('AUI', 'Unique identifier for atom', '8', '8.84', '9', 'MRCONSO.RRF');



-- GOAL 



INSERT INTO alphad (COL,DES,MIN,AV,MAX,FIL)
values ('ATNL', 'Attribute name list for a source.', '0', '28.30', '1050', 'MRSAB.RRF');

INSERT INTO alphad (COL,DES,MIN,AV,MAX,FIL)
values ('AUI1', 'Unique identifier for first atom', '0', '8.52', '9', 'MRREL.RRF');

INSERT INTO alphad (COL,DES,MIN,AV,MAX,FIL)
values ('AUI2', 'Unique identifier for second atom', '0', '8.52', '9', 'MRREL.RRF');

-- HIDDEN TESTS

-- SETUP 2



INSERT INTO alpha2 (COL,DES,MIN,AV,MAX,FIL)
VALUES ('SOURCEUI', 'Source asserted unique identifier', '0', '0.00', '0', 'MRHIST.RRF');

INSERT INTO alpha2 (COL,DES,MIN,AV,MAX,FIL)
VALUES ('SRUI', 'Source attributed relationship identifier', '0', '0.00', '0', 'MRREL.RRF');

INSERT INTO alpha2 (COL,DES,MIN,AV,MAX,FIL)
VALUES ('SUI', 'Unique identifier for string', '0', '0.00', '0', 'MRXW_CZE.RRF');

INSERT INTO alpha2 (COL,DES,MIN,AV,MAX,FIL)
VALUES ('SON', 'Source Official Name', '10', '47.26', '145', 'MRSAB.RRF');

INSERT INTO alpha2 (COL,DES,MIN,AV,MAX,FIL)
VALUES ('SRL', 'Source Restriction Level', '1', '1.00', '1', 'MRCONSO.RRF');

INSERT INTO alpha2 (COL,DES,MIN,AV,MAX,FIL)
VALUES ('SRL', 'Source Restriction Level', '1', '1.00', '1', 'MRSAB.RRF');

INSERT INTO alpha2 (COL,DES,MIN,AV,MAX,FIL)
VALUES ('SSN', 'Source short name', '3', '28.64', '89', 'MRSAB.RRF');

INSERT INTO alpha2 (COL,DES,MIN,AV,MAX,FIL)
VALUES ('STN', 'Semantic type tree number', '1', '7.67', '14', 'MRSTY.RRF');

INSERT INTO alpha2 (COL,DES,MIN,AV,MAX,FIL)
VALUES ('STR', 'String', '1', '38.26', '2936', 'MRCONSO.RRF');

INSERT INTO alpha2 (COL,DES,MIN,AV,MAX,FIL)
VALUES ('STT', 'String type', '2', '2.01', '3', 'MRCONSO.RRF');

INSERT INTO alpha2 (COL,DES,MIN,AV,MAX,FIL)
VALUES ('STYPE1', 'The name of the column in MRCONSO.RRF that contains the first identifier to which the relationship is attached', '3', '3.63', '4', 'MRREL.RRF');

INSERT INTO alpha2 (COL,DES,MIN,AV,MAX,FIL)
VALUES ('STYPE2', 'The name of the column in MRCONSO.RRF that contains the second identifier to which the relationship is attached', '3', '3.63', '4', 'MRREL.RRF');

INSERT INTO alpha2 (COL,DES,MIN,AV,MAX,FIL)
VALUES ('STYPE', 'The name of the column in MRCONSO.RRF or MRREL.RRF that contains the identifier to which the attribute is attached', '3', '3.33', '4', 'MRSAT.RRF');

INSERT INTO alpha2 (COL,DES,MIN,AV,MAX,FIL)
VALUES ('STY', 'Semantic type', '4', '15.20', '39', 'MRSTY.RRF');

-- GOAL 2




INSERT INTO alpha2d (COL,DES,MIN,AV,MAX,FIL)
VALUES ('SOURCEUI', 'Source asserted unique identifier', '0', '0.00', '0', 'MRHIST.RRF');

INSERT INTO alpha2d (COL,DES,MIN,AV,MAX,FIL)
VALUES ('SRUI', 'Source attributed relationship identifier', '0', '0.00', '0', 'MRREL.RRF');

INSERT INTO alpha2d (COL,DES,MIN,AV,MAX,FIL)
VALUES ('SUI', 'Unique identifier for string', '0', '0.00', '0', 'MRXW_CZE.RRF');

-- SETUP 3




INSERT INTO alpha3 (COL,DES,MIN,AV,MAX,FIL)
VALUES ('TOSID', 'Source asserted identifier for the entity being mapped to', '0', '0.00', '0', 'MRMAP.RRF');

INSERT INTO alpha3 (COL,DES,MIN,AV,MAX,FIL)
VALUES ('TTYL', 'Term type list for a source', '0', '6.08', '83', 'MRSAB.RRF');

INSERT INTO alpha3 (COL,DES,MIN,AV,MAX,FIL)
VALUES ('VCUI', 'Unique identifier for versioned SRC concept', '0', '4.43', '8', 'MRSAB.RRF');

INSERT INTO alpha3 (COL,DES,MIN,AV,MAX,FIL)
VALUES ('VEND', 'Valid end date for a source', '0', '0.00', '0', 'MRSAB.RRF');

INSERT INTO alpha3 (COL,DES,MIN,AV,MAX,FIL)
VALUES ('VSTART', 'Valid start date for a source', '0', '0.00', '0', 'MRSAB.RRF');

INSERT INTO alpha3 (COL,DES,MIN,AV,MAX,FIL)
VALUES ('TOTYPE', 'The type of expression that a mapping is mapped to', '4', '4.33', '23', 'MRSMAP.RRF');

INSERT INTO alpha3 (COL,DES,MIN,AV,MAX,FIL)
VALUES ('TOTYPE', 'The type of expression that a mapping is mapped to', '4', '4.38', '23', 'MRMAP.RRF');

INSERT INTO alpha3 (COL,DES,MIN,AV,MAX,FIL)
VALUES ('TS', 'Term status', '1', '1.00', '1', 'MRCONSO.RRF');

INSERT INTO alpha3 (COL,DES,MIN,AV,MAX,FIL)
VALUES ('VALUE', 'Value', '0', '14.77', '62', 'MRDOC.RRF');

INSERT INTO alpha3 (COL,DES,MIN,AV,MAX,FIL)
VALUES ('TTY', 'Term type in source', '2', '2.47', '11', 'MRRANK.RRF');

INSERT INTO alpha3 (COL,DES,MIN,AV,MAX,FIL)
VALUES ('TTY', 'Term type in source', '2', '2.51', '11', 'MRCONSO.RRF');

INSERT INTO alpha3 (COL,DES,MIN,AV,MAX,FIL)
VALUES ('TUI', 'Unique identifier of Semantic type', '4', '4.00', '4', 'MRSTY.RRF');

INSERT INTO alpha3 (COL,DES,MIN,AV,MAX,FIL)
VALUES ('TYPE', 'Type of information', '3', '13.22', '21', 'MRDOC.RRF');

INSERT INTO alpha3 (COL,DES,MIN,AV,MAX,FIL)
VALUES ('VER', 'Last release version in which CUI1 was valid', '6', '6.00', '6', 'MRAUI.RRF');

INSERT INTO alpha3 (COL,DES,MIN,AV,MAX,FIL)
VALUES ('VER', 'Last release version in which CUI1 was valid', '6', '6.00', '6', 'MRCUI.RRF');

INSERT INTO alpha3 (COL,DES,MIN,AV,MAX,FIL)
VALUES ('VSAB', 'Versioned source abbreviation', '3', '12.94', '32', 'MRSAB.RRF');

-- GOAL 3:



INSERT INTO alpha3d (COL,DES,MIN,AV,MAX,FIL)
VALUES ('TOSID', 'Source asserted identifier for the entity being mapped to', '0', '0.00', '0', 'MRMAP.RRF');

INSERT INTO alpha3d (COL,DES,MIN,AV,MAX,FIL)
VALUES ('TTYL', 'Term type list for a source', '0', '6.08', '83', 'MRSAB.RRF');

INSERT INTO alpha3d (COL,DES,MIN,AV,MAX,FIL)
VALUES ('VALUE', 'Value', '0', '14.77', '62', 'MRDOC.RRF');

INSERT INTO alpha3d (COL,DES,MIN,AV,MAX,FIL)
VALUES ('VCUI', 'Unique identifier for versioned SRC concept', '0', '4.43', '8', 'MRSAB.RRF');

INSERT INTO alpha3d (COL,DES,MIN,AV,MAX,FIL)
VALUES ('VEND', 'Valid end date for a source', '0', '0.00', '0', 'MRSAB.RRF');

INSERT INTO alpha3d (COL,DES,MIN,AV,MAX,FIL)
VALUES ('VSTART', 'Valid start date for a source', '0', '0.00', '0', 'MRSAB.RRF');
