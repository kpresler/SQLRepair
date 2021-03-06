-- SELECT & Project

-- SETUP

-- SOLUTION: SELECT RSAB, TFR FROM delta WHERE CFR <= 1634;



INSERT INTO delta (RSAB, SF, TFR, CFR, TTYL)
VALUES ('MTHMSTFRE', 'MTHMST', '1833', '1634', 'PT');

INSERT INTO delta (RSAB, SF, TFR, CFR, TTYL)
VALUES ('MTHMSTITA', 'MTHMST', '1799', '1634', 'PT');

INSERT INTO delta (RSAB, SF, TFR, CFR, TTYL)
VALUES ('ICPCITA', 'ICPC', '723', '722', 'CP,PT');

INSERT INTO delta (RSAB, SF, TFR, CFR, TTYL)
VALUES ('ICPCNOR', 'ICPC', '722', '721', 'CP,PT');

INSERT INTO delta (RSAB, SF, TFR, CFR, TTYL)
VALUES ('CCS', 'CCS', '1617', '1109', 'HT,MD,MV,SD,SP,XM');

INSERT INTO delta (RSAB, SF, TFR, CFR, TTYL)
VALUES ('AOT', 'AOT', '471', '276', 'ET,PT');

INSERT INTO delta (RSAB, SF, TFR, CFR, TTYL)
VALUES ('TKMT', 'TKMT', '770', '761', 'PT');

INSERT INTO delta (RSAB, SF, TFR, CFR, TTYL)
VALUES ('COSTAR', 'COSTAR', '3461', '3086', 'PT');

INSERT INTO delta (RSAB, SF, TFR, CFR, TTYL)
VALUES ('UWDA', 'UWDA', '92913', '61125', 'PT,SY');

INSERT INTO delta (RSAB, SF, TFR, CFR, TTYL)
VALUES ('SPN', 'SPN', '4881', '4810', 'PT');

INSERT INTO delta (RSAB, SF, TFR, CFR, TTYL)
VALUES ('HL7V2.5', 'HL7V2.5', '5019', '4911', 'HTN,PT');

INSERT INTO delta (RSAB, SF, TFR, CFR, TTYL)
VALUES ('CSP', 'CSP', '22775', '16642', 'AB,ET,PT,SY');

INSERT INTO delta (RSAB, SF, TFR, CFR, TTYL)
VALUES ('CHV', 'CHV', '146324', '56353', 'PT,SY');

INSERT INTO delta (RSAB, SF, TFR, CFR, TTYL)
VALUES ('LCH_NW', 'LCH', '13329', '13273', 'PT,XM');

INSERT INTO delta (RSAB, SF, TFR, CFR, TTYL)
VALUES ('ICD9CM', 'ICD9CM', '40855', '20996', 'AB,HT,PT');

INSERT INTO delta (RSAB, SF, TFR, CFR, TTYL)
VALUES ('MTHICD9', 'ICD9CM', '23525', '19120', 'ET');

INSERT INTO delta (RSAB, SF, TFR, CFR, TTYL)
VALUES ('USPMG', 'USPMG', '1703', '1696', 'HC,PT');

INSERT INTO delta (RSAB, SF, TFR, CFR, TTYL)
VALUES ('NCBI', 'NCBI', '1957848', '1409497', 'AUN,CMN,EQ,SCN,SY,UAUN,UCN,UE,USN,USY');

-- GOAL 



INSERT INTO deltad (RSAB, TFR)
VALUES ('MTHMSTFRE', '1833');

INSERT INTO deltad (RSAB, TFR)
VALUES ('MTHMSTITA', '1799');

INSERT INTO deltad (RSAB, TFR)
VALUES ('ICPCITA', '723');

INSERT INTO deltad (RSAB, TFR)
VALUES ('ICPCNOR', '722');

INSERT INTO deltad (RSAB, TFR)
VALUES ('CCS', '1617');

INSERT INTO deltad (RSAB, TFR)
VALUES ('AOT', '471');

INSERT INTO deltad (RSAB, TFR)
VALUES ('TKMT', '770');

