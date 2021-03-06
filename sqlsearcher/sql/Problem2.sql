-- SIMPLE SELECT WITH PROJECTION --

-- SOLUTION: SELECT CUI1, RUI FROM bravo WHERE CUI2 = 'C0364349';

-- SETUP



INSERT INTO bravo (CUI1, AUI1, STYPE1, REL, CUI2, AUI2, STYPE2, RUI) 
values ('C0000039', 'A18399186', 'SCUI', 'RO', 'C0364349', 'A18182073', 'SCUI', 'R165238590');

INSERT INTO bravo (CUI1, AUI1, STYPE1, REL, CUI2, AUI2, STYPE2, RUI) 
values ('C0000039', 'A23513030', 'SCUI', 'RO', 'C0364349', 'A18182073', 'SCUI', 'R167106427');

INSERT INTO bravo (CUI1, AUI1, STYPE1, REL, CUI2, AUI2, STYPE2, RUI) 
values ('C0000039', NULL, 'CUI', 'RO', 'C0364349', NULL, 'CUI', 'R03088111');

INSERT INTO bravo (CUI1, AUI1, STYPE1, REL, CUI2, AUI2, STYPE2, RUI) 
values ('C0000039', 'A1317707', 'AUI', 'SY', 'C0000039', 'A26661070', 'AUI', 'R162647171');

INSERT INTO bravo (CUI1, AUI1, STYPE1, REL, CUI2, AUI2, STYPE2, RUI) 
values ('C0000039', 'A1317708', 'AUI', 'SY', 'C0000039', 'A0016515', 'AUI', 'R28461256');

INSERT INTO bravo (CUI1, AUI1, STYPE1, REL, CUI2, AUI2, STYPE2, RUI) 
values ('C0000039', 'A17972824', 'SCUI', 'CHD', 'C0216971', 'A17882534', 'SCUI', 'R119341892');

INSERT INTO bravo (CUI1, AUI1, STYPE1, REL, CUI2, AUI2, STYPE2, RUI) 
values ('C0000039', 'A17972824', 'SCUI', 'PAR', 'C1959616', 'A17889791', 'SCUI', 'R119384882');

INSERT INTO bravo (CUI1, AUI1, STYPE1, REL, CUI2, AUI2, STYPE2, RUI) 
values ('C0000039', 'A26631676', 'AUI', 'SY', 'C0000039', 'A0100864', 'AUI', 'R162750769');

INSERT INTO bravo (CUI1, AUI1, STYPE1, REL, CUI2, AUI2, STYPE2, RUI) 
values ('C0000039', 'A26661070', 'AUI', 'SY', 'C0000039', 'A1317707', 'AUI', 'R162608734');

INSERT INTO bravo (CUI1, AUI1, STYPE1, REL, CUI2, AUI2, STYPE2, RUI) 
values ('C0000039', 'A26674543', 'AUI', 'SY', 'C0000039', 'A1317687', 'AUI', 'R162756710');

INSERT INTO bravo (CUI1, AUI1, STYPE1, REL, CUI2, AUI2, STYPE2, RUI) 
values ('C0000039', 'A28315139', 'SCUI', 'PAR', 'C2267088', 'A28940433', 'SCUI', 'R176814915');

INSERT INTO bravo (CUI1, AUI1, STYPE1, REL, CUI2, AUI2, STYPE2, RUI) 
values ('C0000039', 'A28315139', 'SCUI', 'RN', 'C4489915', 'A28312270', 'SCUI', 'R173136536');

INSERT INTO bravo (CUI1, AUI1, STYPE1, REL, CUI2, AUI2, STYPE2, RUI) 
values ('C0000039', 'A28315139', 'SCUI', 'SY', 'C0000039', 'A0016515', 'SCUI', 'R179347546');

INSERT INTO bravo (CUI1, AUI1, STYPE1, REL, CUI2, AUI2, STYPE2, RUI) 
values ('C0000052', 'A0016529', 'AUI', 'SY', 'C0000052', 'A0016535', 'AUI', 'R28409371');

INSERT INTO bravo (CUI1, AUI1, STYPE1, REL, CUI2, AUI2, STYPE2, RUI) 
values ('C0000052', 'A0016535', 'AUI', 'SY', 'C0000052', 'A0016529', 'AUI', 'R28482440');

INSERT INTO bravo (CUI1, AUI1, STYPE1, REL, CUI2, AUI2, STYPE2, RUI) 
values ('C0000052', 'A0016535', 'AUI', 'SY', 'C0000052', 'A12072980', 'AUI', 'R64592884');

INSERT INTO bravo (CUI1, AUI1, STYPE1, REL, CUI2, AUI2, STYPE2, RUI) 
values ('C0000052', 'A0016535', 'AUI', 'SY', 'C0000052', 'A12076643', 'AUI', 'R64579230');

INSERT INTO bravo (CUI1, AUI1, STYPE1, REL, CUI2, AUI2, STYPE2, RUI) 
values ('C0000052', 'A0016535', 'AUI', 'SY', 'C0000052', 'A1945338', 'AUI', 'R28482441');

-- GOAL




INSERT INTO bravod (CUI1, RUI)
VALUES ('C0000039', 'R165238590');

INSERT INTO bravod (CUI1, RUI)
VALUES ('C0000039', 'R167106427');

INSERT INTO bravod (CUI1, RUI)
VALUES ('C0000039', 'R03088111');

-- HIDDEN TESTS

-- Setup 2:




INSERT INTO bravo2 (CUI1, AUI1, STYPE1, REL, CUI2, AUI2, STYPE2, RUI) 
VALUES ('C0364349', 'A18182073', 'AUI', 'SY', 'C0364349', 'A18297855', 'AUI', 'R118308873');

INSERT INTO bravo2 (CUI1, AUI1, STYPE1, REL, CUI2, AUI2, STYPE2, RUI) 
VALUES ('C0560150', 'A18367132', 'SCUI', 'RO', 'C0364349', 'A18182073', 'SCUI', 'R166372213');

INSERT INTO bravo2 (CUI1, AUI1, STYPE1, REL, CUI2, AUI2, STYPE2, RUI) 
VALUES ('C0000039', 'A23513030', 'SCUI', 'RO', 'C0364349', 'A18182073', 'SCUI', 'R167106427');

INSERT INTO bravo2 (CUI1, AUI1, STYPE1, REL, CUI2, AUI2, STYPE2, RUI) 
VALUES ('C0000039', 'A0016515', 'SDUI', 'AQ', 'C0001688', 'A3879703', 'SDUI', 'R120502286');

INSERT INTO bravo2 (CUI1, AUI1, STYPE1, REL, CUI2, AUI2, STYPE2, RUI) 
VALUES ('C0000039', 'A0016515', 'SDUI', 'AQ', 'C0002776', 'A3879704', 'SDUI', 'R120502287');

INSERT INTO bravo2 (CUI1, AUI1, STYPE1, REL, CUI2, AUI2, STYPE2, RUI) 
VALUES ('C0000039', 'A0016515', 'SDUI', 'RN', 'C0381030', 'A0683149', 'SDUI', 'R148166148');

INSERT INTO bravo2 (CUI1, AUI1, STYPE1, REL, CUI2, AUI2, STYPE2, RUI) 
VALUES ('C0000039', 'A0016515', 'SDUI', 'RN', 'C0615231', 'A1316792', 'SDUI', 'R148263106');

INSERT INTO bravo2 (CUI1, AUI1, STYPE1, REL, CUI2, AUI2, STYPE2, RUI) 
VALUES ('C0000039', 'A0016515', 'SDUI', 'RN', 'C0621533', 'A1321548', 'SDUI', 'R128968098');

INSERT INTO bravo2 (CUI1, AUI1, STYPE1, REL, CUI2, AUI2, STYPE2, RUI) 
VALUES ('C0000039', 'A0016515', 'SDUI', 'RN', 'C1310941', 'A3844843', 'SDUI', 'R148167162');

INSERT INTO bravo2 (CUI1, AUI1, STYPE1, REL, CUI2, AUI2, STYPE2, RUI) 
VALUES ('C0000039', 'A0016515', 'SDUI', 'RN', 'C1611431', 'A8605126', 'SDUI', 'R148134981');

INSERT INTO bravo2 (CUI1, AUI1, STYPE1, REL, CUI2, AUI2, STYPE2, RUI) 
VALUES ('C0000039', 'A0016515', 'SDUI', 'RN', 'C3253442', 'A20045770', 'SDUI', 'R148232429');

INSERT INTO bravo2 (CUI1, AUI1, STYPE1, REL, CUI2, AUI2, STYPE2, RUI) 
VALUES ('C0000039', 'A0016515', 'SDUI', 'RN', 'C3885037', 'A24312540', 'SDUI', 'R154017721');

INSERT INTO bravo2 (CUI1, AUI1, STYPE1, REL, CUI2, AUI2, STYPE2, RUI) 
VALUES ('C0000039', 'A0016515', 'SDUI', 'SIB', 'C0012456', 'A0049151', 'SDUI', 'R71241360');

INSERT INTO bravo2 (CUI1, AUI1, STYPE1, REL, CUI2, AUI2, STYPE2, RUI) 
VALUES ('C0000039', 'A0016515', 'SDUI', 'SIB', 'C0031617', 'A12995624', 'SDUI', 'R71104054');

-- GOAL 2




INSERT INTO bravo2d (CUI1, RUI)
VALUES ('C0364349', 'R118308873');

INSERT INTO bravo2d (CUI1, RUI)
VALUES ('C0560150', 'R166372213');

INSERT INTO bravo2d (CUI1, RUI)
VALUES ('C0000039', 'R167106427');
