-- ORDER BY

-- SOLUTION: SELECT * FROM hotel ORDER BY TUI DESC, CUI ASC;

-- SETUP:




INSERT INTO hotel (CUI, TUI, STN) VALUES ('C0000005', 'T130', 'A1.4.1.1.4');

INSERT INTO hotel (CUI, TUI, STN) VALUES ('C0000039', 'T109', 'A1.4.1.2.1');

INSERT INTO hotel (CUI, TUI, STN) VALUES ('C0000039', 'T121', 'A1.4.1.1.1');

INSERT INTO hotel (CUI, TUI, STN) VALUES ('C0000052', 'T116', 'A1.4.1.2.1.7');

INSERT INTO hotel (CUI, TUI, STN) VALUES ('C0000052', 'T126', 'A1.4.1.1.3.3');

INSERT INTO hotel (CUI, TUI, STN) VALUES ('C0000074', 'T109', 'A1.4.1.2.1');

INSERT INTO hotel (CUI, TUI, STN) VALUES ('C0000084', 'T116', 'A1.4.1.2.1.7');

INSERT INTO hotel (CUI, TUI, STN) VALUES ('C0000084', 'T123', 'A1.4.1.1.3');

INSERT INTO hotel (CUI, TUI, STN) VALUES ('C0000096', 'T109', 'A1.4.1.2.1');

INSERT INTO hotel (CUI, TUI, STN) VALUES ('C0000096', 'T121', 'A1.4.1.1.1');

INSERT INTO hotel (CUI, TUI, STN) VALUES ('C0000097', 'T109', 'A1.4.1.2.1');

INSERT INTO hotel (CUI, TUI, STN) VALUES ('C0000097', 'T131', 'A1.4.1.1.5');

INSERT INTO hotel (CUI, TUI, STN) VALUES ('C0000098', 'T109', 'A1.4.1.2.1');

INSERT INTO hotel (CUI, TUI, STN) VALUES ('C0000098', 'T131', 'A1.4.1.1.5');

INSERT INTO hotel (CUI, TUI, STN) VALUES ('C0000102', 'T109', 'A1.4.1.2.1');

INSERT INTO hotel (CUI, TUI, STN) VALUES ('C0000102', 'T131', 'A1.4.1.1.5');

-- GOAL:





INSERT INTO hoteld (CUI, TUI, STN) VALUES ('C0000097', 'T131', 'A1.4.1.1.5');

INSERT INTO hoteld (CUI, TUI, STN) VALUES ('C0000098', 'T131', 'A1.4.1.1.5');

INSERT INTO hoteld (CUI, TUI, STN) VALUES ('C0000102', 'T131', 'A1.4.1.1.5');

INSERT INTO hoteld (CUI, TUI, STN) VALUES ('C0000005', 'T130', 'A1.4.1.1.4');

INSERT INTO hoteld (CUI, TUI, STN) VALUES ('C0000052', 'T126', 'A1.4.1.1.3.3');

INSERT INTO hoteld (CUI, TUI, STN) VALUES ('C0000084', 'T123', 'A1.4.1.1.3');

INSERT INTO hoteld (CUI, TUI, STN) VALUES ('C0000039', 'T121', 'A1.4.1.1.1');

INSERT INTO hoteld (CUI, TUI, STN) VALUES ('C0000096', 'T121', 'A1.4.1.1.1');

INSERT INTO hoteld (CUI, TUI, STN) VALUES ('C0000052', 'T116', 'A1.4.1.2.1.7');

INSERT INTO hoteld (CUI, TUI, STN) VALUES ('C0000084', 'T116', 'A1.4.1.2.1.7');

INSERT INTO hoteld (CUI, TUI, STN) VALUES ('C0000039', 'T109', 'A1.4.1.2.1');

INSERT INTO hoteld (CUI, TUI, STN) VALUES ('C0000074', 'T109', 'A1.4.1.2.1');

INSERT INTO hoteld (CUI, TUI, STN) VALUES ('C0000096', 'T109', 'A1.4.1.2.1');

INSERT INTO hoteld (CUI, TUI, STN) VALUES ('C0000097', 'T109', 'A1.4.1.2.1');

INSERT INTO hoteld (CUI, TUI, STN) VALUES ('C0000098', 'T109', 'A1.4.1.2.1');

INSERT INTO hoteld (CUI, TUI, STN) VALUES ('C0000102', 'T109', 'A1.4.1.2.1');

-- HIDDEN TESTS

-- SETUP 2


INSERT INTO hotel2 (CUI, TUI, STN) VALUES ('C0000855', 'T121', 'A1.4.1.1.1');

INSERT INTO hotel2 (CUI, TUI, STN) VALUES ('C0000855', 'T197', 'A1.4.1.2.2');

INSERT INTO hotel2 (CUI, TUI, STN) VALUES ('C0000857', 'T057', 'B1.3');

INSERT INTO hotel2 (CUI, TUI, STN) VALUES ('C0000858', 'T057', 'B1.3');

INSERT INTO hotel2 (CUI, TUI, STN) VALUES ('C0000863', 'T083', 'A2.1.5.4');

INSERT INTO hotel2 (CUI, TUI, STN) VALUES ('C0000864', 'T055', 'B1.1.2');

INSERT INTO hotel2 (CUI, TUI, STN) VALUES ('C0000866', 'T074', 'A1.3.1');

INSERT INTO hotel2 (CUI, TUI, STN) VALUES ('C0000869', 'T002', 'A1.1.3.3');

INSERT INTO hotel2 (CUI, TUI, STN) VALUES ('C0000872', 'T073', 'A1.3');

INSERT INTO hotel2 (CUI, TUI, STN) VALUES ('C0000872', 'T093', 'A2.7.1');

INSERT INTO hotel2 (CUI, TUI, STN) VALUES ('C0000873', 'T033', 'A2.2');

INSERT INTO hotel2 (CUI, TUI, STN) VALUES ('C0000875', 'T065', 'B1.3.4');

INSERT INTO hotel2 (CUI, TUI, STN) VALUES ('C0000876', 'T092', 'A2.7');

INSERT INTO hotel2 (CUI, TUI, STN) VALUES ('C0000877', 'T092', 'A2.7');

INSERT INTO hotel2 (CUI, TUI, STN) VALUES ('C0000879', 'T204', 'A1.1.3');

INSERT INTO hotel2 (CUI, TUI, STN) VALUES ('C0000880', 'T047', 'B2.2.1.2.1');

INSERT INTO hotel2 (CUI, TUI, STN) VALUES ('C0000881', 'T204', 'A1.1.3');

INSERT INTO hotel2 (CUI, TUI, STN) VALUES ('C0000886', 'T025', 'A1.2.3.3');

INSERT INTO hotel2 (CUI, TUI, STN) VALUES ('C0000887', 'T046', 'B2.2.1.2');

INSERT INTO hotel2 (CUI, TUI, STN) VALUES ('C0000889', 'T047', 'B2.2.1.2.1');

INSERT INTO hotel2 (CUI, TUI, STN) VALUES ('C0000890', 'T204', 'A1.1.3');

INSERT INTO hotel2 (CUI, TUI, STN) VALUES ('C0000892', 'T204', 'A1.1.3');

INSERT INTO hotel2 (CUI, TUI, STN) VALUES ('C0000893', 'T196', 'A1.4.1.2.3');

INSERT INTO hotel2 (CUI, TUI, STN) VALUES ('C0000894', 'T067', 'B2');

INSERT INTO hotel2 (CUI, TUI, STN) VALUES ('C0000895', 'T060', 'B1.3.1.2');

INSERT INTO hotel2 (CUI, TUI, STN) VALUES ('C0000898', 'T080', 'A2.1.2');

INSERT INTO hotel2 (CUI, TUI, STN) VALUES ('C0000899', 'T055', 'B1.1.2');

INSERT INTO hotel2 (CUI, TUI, STN) VALUES ('C0000901', 'T098', 'A2.9.2');

INSERT INTO hotel2 (CUI, TUI, STN) VALUES ('C0000905', 'T023', 'A1.2.3.1');

-- GOAL 2






INSERT INTO hotel2d (CUI, TUI, STN) VALUES ('C0000879', 'T204', 'A1.1.3');

INSERT INTO hotel2d (CUI, TUI, STN) VALUES ('C0000881', 'T204', 'A1.1.3');

INSERT INTO hotel2d (CUI, TUI, STN) VALUES ('C0000890', 'T204', 'A1.1.3');

INSERT INTO hotel2d (CUI, TUI, STN) VALUES ('C0000892', 'T204', 'A1.1.3');

INSERT INTO hotel2d (CUI, TUI, STN) VALUES ('C0000855', 'T197', 'A1.4.1.2.2');

INSERT INTO hotel2d (CUI, TUI, STN) VALUES ('C0000893', 'T196', 'A1.4.1.2.3');

INSERT INTO hotel2d (CUI, TUI, STN) VALUES ('C0000855', 'T121', 'A1.4.1.1.1');

INSERT INTO hotel2d (CUI, TUI, STN) VALUES ('C0000901', 'T098', 'A2.9.2');

INSERT INTO hotel2d (CUI, TUI, STN) VALUES ('C0000872', 'T093', 'A2.7.1');

INSERT INTO hotel2d (CUI, TUI, STN) VALUES ('C0000876', 'T092', 'A2.7');

INSERT INTO hotel2d (CUI, TUI, STN) VALUES ('C0000877', 'T092', 'A2.7');

INSERT INTO hotel2d (CUI, TUI, STN) VALUES ('C0000863', 'T083', 'A2.1.5.4');

INSERT INTO hotel2d (CUI, TUI, STN) VALUES ('C0000898', 'T080', 'A2.1.2');

INSERT INTO hotel2d (CUI, TUI, STN) VALUES ('C0000866', 'T074', 'A1.3.1');

INSERT INTO hotel2d (CUI, TUI, STN) VALUES ('C0000872', 'T073', 'A1.3');

INSERT INTO hotel2d (CUI, TUI, STN) VALUES ('C0000894', 'T067', 'B2');

INSERT INTO hotel2d (CUI, TUI, STN) VALUES ('C0000875', 'T065', 'B1.3.4');

INSERT INTO hotel2d (CUI, TUI, STN) VALUES ('C0000895', 'T060', 'B1.3.1.2');

INSERT INTO hotel2d (CUI, TUI, STN) VALUES ('C0000857', 'T057', 'B1.3');

INSERT INTO hotel2d (CUI, TUI, STN) VALUES ('C0000858', 'T057', 'B1.3');

INSERT INTO hotel2d (CUI, TUI, STN) VALUES ('C0000864', 'T055', 'B1.1.2');

INSERT INTO hotel2d (CUI, TUI, STN) VALUES ('C0000899', 'T055', 'B1.1.2');

INSERT INTO hotel2d (CUI, TUI, STN) VALUES ('C0000880', 'T047', 'B2.2.1.2.1');

INSERT INTO hotel2d (CUI, TUI, STN) VALUES ('C0000889', 'T047', 'B2.2.1.2.1');

INSERT INTO hotel2d (CUI, TUI, STN) VALUES ('C0000887', 'T046', 'B2.2.1.2');

INSERT INTO hotel2d (CUI, TUI, STN) VALUES ('C0000873', 'T033', 'A2.2');

INSERT INTO hotel2d (CUI, TUI, STN) VALUES ('C0000886', 'T025', 'A1.2.3.3');

INSERT INTO hotel2d (CUI, TUI, STN) VALUES ('C0000905', 'T023', 'A1.2.3.1');

INSERT INTO hotel2d (CUI, TUI, STN) VALUES ('C0000869', 'T002', 'A1.1.3.3');
