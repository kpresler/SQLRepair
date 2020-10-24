package edu.ncsu.sqlsearcher.controllers;

import java.util.ArrayList;
import java.util.List;

import edu.ncsu.sqlsearcher.models.SQLStatement;
import edu.ncsu.z3sqlparser.SQLTableExample;
import edu.ncsu.z3sqlparser.SQLTableExamples;
import edu.ncsu.z3sqlparser.controllers.APIZ3Controller;

public class QueryRepairer {

	public static String repairAQuery(String whichProblem, final SQLStatement query) {

		APIZ3Controller ctrl = new APIZ3Controller();

		SQLTableExamples examples = null;

		List<SQLTableExample> exs = new ArrayList<SQLTableExample>();
		SQLTableExample ex = new SQLTableExample();

		switch (whichProblem) {
		case "Problem 1":
			ex.setSource("COL DES MIN AV MAX FIL\n" + "ATNL Attribute_name_list_for_a_source. 0 28.30 1050 MRSAB.RRF\n"
					+ "AUI1 Unique_identifier_for_first_atom 0 8.52 9 MRREL.RRF\n"
					+ "AUI2 Unique_identifier_for_second_atom 0 8.52 9 MRREL.RRF\n"
					+ "ATN Attribute_name 2 5.44 62 MRSAT.RRF\n"
					+ "ATUI Unique_identifier_for_attribute. 10 10.66 11 MRSTY.RRF\n"
					+ "ATUI Unique_identifier_for_attribute. 10 10.77 11 MRSAT.RRF\n"
					+ "ATUI Unique_identifier_for_attribute. 10 10.83 11 MRDEF.RRF\n"
					+ "ATV Attribute_value 1 11.65 3997 MRSAT.RRF\n"
					+ "AUI1 Unique_identifier_for_first_atom 8 8.52 9 MRAUI.RRF\n"
					+ "AUI2 Unique_identifier_for_second_atom 8 8.52 9 MRAUI.RRF\n"
					+ "AUI Unique_identifier_for_atom 8 8.75 9 MRDEF.RRF\n"
					+ "AUI Unique_identifier_for_atom 8 8.78 9 MRHIER.RRF\n"
					+ "AUI Unique_identifier_for_atom 8 8.84 9 MRCONSO.RRF\n");

			ex.setDest("COL DES MIN AV MAX FIL\n" + "ATNL Attribute_name_list_for_a_source. 0 28.30 1050 MRSAB.RRF\n"
					+ "AUI1 Unique_identifier_for_first_atom 0 8.52 9 MRREL.RRF\n"
					+ "AUI2 Unique_identifier_for_second_atom 0 8.52 9 MRREL.RRF\n");

			exs.add(ex);

			ex = new SQLTableExample();

			ex.setSource("COL DES MIN AV MAX FIL\n" + "SOURCEUI Source_asserted_unique_identifier 0 0.00 0 MRHIST.RRF\n"
					+ "SRUI Source_attributed_relationship_identifier 0 0.00 0 MRREL.RRF\n"
					+ "SUI Unique_identifier_for_string 0 0.00 0 MRXW_CZE.RRF\n"
					+ "SON Source_Official_Name 10 47.26 145 MRSAB.RRF\n"
					+ "SRL Source_Restriction_Level 1 1.00 1 MRCONSO.RRF\n"
					+ "SRL Source_Restriction_Level 1 1.00 1 MRSAB.RRF\n"
					+ "SSN Source_short_name 3 28.64 89 MRSAB.RRF\n"
					+ "STN Semantic_type_tree_number 1 7.67 14 MRSTY.RRF\n" + "STR String 1 38.26 2936 MRCONSO.RRF\n"
					+ "STT String_type 2 2.01 3 MRCONSO.RRF\n"
					+ "STYPE1 The_name_of_the_column_in_MRCONSO.RRF_that_contains_the_first_identifier_to_which_the_relationship_is_attached 3 3.63 4 MRREL.RRF\n"
					+ "STYPE2 The_name_of_the_column_in_MRCONSO.RRF_that_contains_the_second_identifier_to_which_the_relationship_is_attached 3 3.63 4 MRREL.RRF\n"
					+ "STYPE The_name_of_the_column_in_MRCONSO.RRF_or_MRREL.RRF_that_contains_the_identifier_to_which_the_attribute_is_attached 3 3.33 4 MRSAT.RRF\n"
					+ "STY Semantic_type 4 15.20 39 MRSTY.RRF\n");

			ex.setDest("COL DES MIN AV MAX FIL\n" + "SOURCEUI Source_asserted_unique_identifier 0 0.00 0 MRHIST.RRF\n"
					+ "SRUI Source_attributed_relationship_identifier 0 0.00 0 MRREL.RRF\n"
					+ "SUI Unique_identifier_for_string 0 0.00 0 MRXW_CZE.RRF\n");

			exs.add(ex);

			ex = new SQLTableExample();

			ex.setSource("COL DES MIN AV MAX FIL\n"
					+ "TOSID Source_asserted_identifier_for_the_entity_being_mapped_to 0 0.00 0 MRMAP.RRF\n"
					+ "TTYL Term_type_list_for_a_source 0 6.08 83 MRSAB.RRF\n" + "VALUE Value 0 14.77 62 MRDOC.RRF\n"
					+ "VCUI Unique_identifier_for_versioned_SRC_concept 0 4.43 8 MRSAB.RRF\n"
					+ "VEND Valid_end_date_for_a_source 0 0.00 0 MRSAB.RRF\n"
					+ "VSTART Valid_start_date_for_a_source 0 0.00 0 MRSAB.RRF\n"
					+ "TOTYPE The_type_of_expression_that_a_mapping_is_mapped_to 4 4.33 23 MRSMAP.RRF\n"
					+ "TOTYPE The_type_of_expression_that_a_mapping_is_mapped_to 4 4.38 23 MRMAP.RRF\n"
					+ "TS Term_status 1 1.00 1 MRCONSO.RRF\n" + "TTY Term_type_in_source 2 2.47 11 MRRANK.RRF\n"
					+ "TTY Term_type_in_source 2 2.51 11 MRCONSO.RRF\n"
					+ "TUI Unique_identifier_of_Semantic_type 4 4.00 4 MRSTY.RRF\n"
					+ "TYPE Type_of_information 3 13.22 21 MRDOC.RRF\n"
					+ "VER Last_release_version_in_which_CUI1_was_valid 6 6.00 6 MRAUI.RRF\n"
					+ "VER Last_release_version_in_which_CUI1_was_valid 6 6.00 6 MRCUI.RRF\n"
					+ "VSAB Versioned_source_abbreviation 3 12.94 32 MRSAB.RRF\n");

			ex.setDest("COL DES MIN AV MAX FIL\n"
					+ "TOSID Source_asserted_identifier_for_the_entity_being_mapped_to 0 0.00 0 MRMAP.RRF\n"
					+ "TTYL Term_type_list_for_a_source 0 6.08 83 MRSAB.RRF\n" + "VALUE Value 0 14.77 62 MRDOC.RRF\n"
					+ "VCUI Unique_identifier_for_versioned_SRC_concept 0 4.43 8 MRSAB.RRF\n"
					+ "VEND Valid_end_date_for_a_source 0 0.00 0 MRSAB.RRF\n"
					+ "VSTART Valid_start_date_for_a_source 0 0.00 0 MRSAB.RRF\n");
			exs.add(ex);

			examples = new SQLTableExamples(exs, query.getStatement());
			break;

		case "Problem 2":
			ex.setSource("CUI1 RUI AUI1 STYPE1 REL CUI2 AUI2 STYPE2\n"
					+ "C0000039 R165238590 A18399186 SCUI RO C0364349 A18182073 SCUI\n"
					+ "C0000039 R167106427 A23513030 SCUI RO C0364349 A18182073 SCUI\n"
					+ "C0000039 R03088111 NULL CUI RO C0364349 NULL CUI\n"
					+ "C0000039 R162647171 A1317707 AUI SY C0000039 A26661070 AUI\n"
					+ "C0000039 R28461256 A1317708 AUI SY C0000039 A0016515 AUI\n"
					+ "C0000039 R119341892 A17972824 SCUI CHD C0216971 A17882534 SCUI\n"
					+ "C0000039 R119384882 A17972824 SCUI PAR C1959616 A17889791 SCUI\n"
					+ "C0000039 R162750769 A26631676 AUI SY C0000039 A0100864 AUI\n"
					+ "C0000039 R162608734 A26661070 AUI SY C0000039 A1317707 AUI\n"
					+ "C0000039 R162756710 A26674543 AUI SY C0000039 A1317687 AUI\n"
					+ "C0000039 R176814915 A28315139 SCUI PAR C2267088 A28940433 SCUI\n"
					+ "C0000039 R173136536 A28315139 SCUI RN C4489915 A28312270 SCUI\n"
					+ "C0000039 R179347546 A28315139 SCUI SY C0000039 A0016515 SCUI\n"
					+ "C0000052 R28409371 A0016529 AUI SY C0000052 A0016535 AUI\n"
					+ "C0000052 R28482440 A0016535 AUI SY C0000052 A0016529 AUI\n"
					+ "C0000052 R64592884 A0016535 AUI SY C0000052 A12072980 AUI\n"
					+ "C0000052 R64579230 A0016535 AUI SY C0000052 A12076643 AUI\n"
					+ "C0000052 R28482441 A0016535 AUI SY C0000052 A1945338 AUI\n");

			ex.setDest("CUI1 RUI\n" + "C0000039 R165238590\n" + "C0000039 R167106427\n" + "C0000039 R03088111\n");

			exs.add(ex);

			ex = new SQLTableExample();

			ex.setSource("CUI1 RUI AUI1 STYPE1 REL CUI2 AUI2 STYPE2\n"
					+ "C0364349 R118308873 A18182073 AUI SY C0364349 A18297855 AUI\n"
					+ "C0560150 R166372213 A18367132 SCUI RO C0364349 A18182073 SCUI\n"
					+ "C0000039 R167106427 A23513030 SCUI RO C0364349 A18182073 SCUI\n"
					+ "C0000039 R120502286 A0016515 SDUI AQ C0001688 A3879703 SDUI\n"
					+ "C0000039 R120502287 A0016515 SDUI AQ C0002776 A3879704 SDUI\n"
					+ "C0000039 R148166148 A0016515 SDUI RN C0381030 A0683149 SDUI\n"
					+ "C0000039 R148263106 A0016515 SDUI RN C0615231 A1316792 SDUI\n"
					+ "C0000039 R128968098 A0016515 SDUI RN C0621533 A1321548 SDUI\n"
					+ "C0000039 R148167162 A0016515 SDUI RN C1310941 A3844843 SDUI\n"
					+ "C0000039 R148134981 A0016515 SDUI RN C1611431 A8605126 SDUI\n"
					+ "C0000039 R148232429 A0016515 SDUI RN C3253442 A20045770 SDUI\n"
					+ "C0000039 R154017721 A0016515 SDUI RN C3885037 A24312540 SDUI\n"
					+ "C0000039 R71241360 A0016515 SDUI SIB C0012456 A0049151 SDUI\n"
					+ "C0000039 R71104054 A0016515 SDUI SIB C0031617 A12995624 SDUI\n");

			ex.setDest("CUI1 RUI\n" + "C0364349 R118308873\n" + "C0560150 R166372213\n" + "C0000039 R167106427\n");

			exs.add(ex);
			examples = new SQLTableExamples(exs, query.getStatement());
			break;

		case "Problem 3":

			ex.setSource("RSAB SF TFR CFR TTYL\n" + "MTHMSTFRE MTHMST 1833 1634 PT\n"
					+ "MTHMSTITA MTHMST 1799 1634 PT\n" + "ICPCITA ICPC 723 722 CPPT\n" + "ICPCNOR ICPC 722 721 CPPT\n"
					+ "CCS CCS 1617 1109 HTMDMVSDSPXM\n" + "AOT AOT 471 276 ETPT\n" + "TKMT TKMT 770 761 PT\n"
					+ "COSTAR COSTAR 3461 3086 PT\n" + "UWDA UWDA 92913 61125 PTSY\n" + "SPN SPN 4881 4810 PT\n"
					+ "HL7V2.5 HL7V2.5 5019 4911 HTNPT\n" + "CSP CSP 22775 16642 ABETPTSY\n"
					+ "CHV CHV 146324 56353 PTSY\n" + "LCH_NW LCH 13329 13273 PTXM\n"
					+ "ICD9CM ICD9CM 40855 20996 ABHTPT\n" + "MTHICD9 ICD9CM 23525 19120 ET\n"
					+ "USPMG USPMG 1703 1696 HCPT\n" + "NCBI NCBI 1957848 1409497 AUNCMNEQSCNSYUAUNUCNUEUSNUSY\n");

			ex.setDest("RSAB SF TFR CFR TTYL\n" + "MTHMSTFRE MTHMST 1833 1634 PT\n" + "MTHMSTITA MTHMST 1799 1634 PT\n"
					+ "ICPCITA ICPC 723 722 CPPT\n" + "ICPCNOR ICPC 722 721 CPPT\n" + "CCS CCS 1617 1109 HTMDMVSDSPXM\n"
					+ "AOT AOT 471 276 ETPT\n" + "TKMT TKMT 770 761 PT\n");

			exs.add(ex);

			ex = new SQLTableExample();

			ex.setSource("RSAB SF TFR CFR TTYL\n" + "NCI_CRCH NCI 868 411 PTSY\n" + "NCI_DTP NCI 1964 1324 PTSY\n"
					+ "NCI_DICOM NCI 114 114 PT\n" + "NCI_ICH NCI 500 215 ABPTSY\n" + "NCI_DCP NCI 908 904 PTSY\n"
					+ "NCI_JAX NCI 156 156 PTSY\n" + "NCI_CTEP-SDC NCI 463 373 PTSY\n" + "NCI_NCPDP NCI 544 539 PTSY\n"
					+ "NCI_KEGG NCI 278 244 ABPTSY\n" + "NCI_CDISC-GLOSS NCI 781 710 PTSY\n"
					+ "NCI_CPTAC NCI 463 455 ABPTSY\n" + "NCI_INC NCI 201 201 PT\n" + "NCI_NICHD NCI 7377 4764 OPPTSY\n"
					+ "NCI_FDA NCI 23955 21642 ABPTSY\n" + "NCI_CDISC NCI 48744 20578 PTSY\n"
					+ "NCI_NCI-HGNC NCI 4894 4893 PTSY\n" + "NCI_CTRP NCI 20500 20261 DNPTSY\n");

			ex.setDest("RSAB SF TFR CFR TTYL\n" + "NCI_CRCH NCI 868 411 PTSY\n" + "NCI_DTP NCI 1964 1324 PTSY\n"
					+ "NCI_DICOM NCI 114 114 PT\n" + "NCI_ICH NCI 500 215 ABPTSY\n" + "NCI_DCP NCI 908 904 PTSY\n"
					+ "NCI_JAX NCI 156 156 PTSY\n" + "NCI_CTEP-SDC NCI 463 373 PTSY\n" + "NCI_NCPDP NCI 544 539 PTSY\n"
					+ "NCI_KEGG NCI 278 244 ABPTSY\n" + "NCI_CDISC-GLOSS NCI 781 710 PTSY\n"
					+ "NCI_CPTAC NCI 463 455 ABPTSY\n" + "NCI_INC NCI 201 201 PT\n");

			exs.add(ex);

			ex = new SQLTableExample();

			ex.setSource("RSAB SF TFR CFR TTYL\n" + "CCS CCS 1617 1109 HTMDMVSDSPXM\n" + "AOT AOT 471 276 ETPT\n"
					+ "TKMT TKMT 770 761 PT\n" + "SOP SOP 162 162 PT\n" + "MVX MVX 77 76 PT\n"
					+ "HL7V2.5 HL7V2.5 5019 4911 HTNPT\n" + "CSP CSP 22775 16642 ABETPTSY\n"
					+ "CHV CHV 146324 56353 PTSY\n" + "LCH_NW LCH 13329 13273 PTXM\n"
					+ "ICD9CM ICD9CM 40855 20996 ABHTPT\n" + "MTHICD9 ICD9CM 23525 19120 ET\n"
					+ "USPMG USPMG 1703 1696 HCPT\n" + "NCBI NCBI 1957848 1409497 AUNCMNEQSCNSYUAUNUCNUEUSNUSY\n"
					+ "FMA FMA 171017 102424 ABISOPPTSY\n" + "HGNC HGNC 173731 41304 ACRMTH_ACRNAPTSYN\n"
					+ "GO GO 178923 70302 ETISMTH_ETMTH_ISMTH_OETMTH_OPMTH_PTMTH_SYOETOPPTSY\n"
					+ "ICD10PCS ICD10PCS 349906 190389 ABHSHTHXMTH_HXPTPX\n"
					+ "HCPCS HCPCS 13610 6656 ABAMMPOAOAMOMOPPT\n");

			ex.setDest("RSAB SF TFR CFR TTYL\n" + "CCS CCS 1617 1109 HTMDMVSDSPXM\n" + "AOT AOT 471 276 ETPT\n"
					+ "TKMT TKMT 770 761 PT\n" + "SOP SOP 162 162 PT\n" + "MVX MVX 77 76 PT\n");
			exs.add(ex);

			examples = new SQLTableExamples(exs, query.getStatement());
			break;

		case "Problem 4":

			ex.setSource("RSAB TFR SF CFR TTYL\n" + "MTHMSTFRE 1833 MTHMST 1634 PT\n"
					+ "MTHMSTITA 1799 MTHMST 1634 PT\n" + "ICPCITA 723 ICPC 722 CPPT\n" + "ICPCNOR 722 ICPC 721 CPPT\n"
					+ "CCS 1617 CCS 1109 HTMDMVSDSPXM\n" + "AOT 471 AOT 276 ETPT\n" + "TKMT 770 TKMT 761 PT\n"
					+ "COSTAR 3461 COSTAR 3086 PT\n" + "UWDA 92913 UWDA 61125 PTSY\n" + "SPN 4881 SPN 4810 PT\n"
					+ "HL7V2.5 5019 HL7V2.5 4911 HTNPT\n" + "CSP 22775 CSP 16642 ABETPTSY\n"
					+ "CHV 146324 CHV 56353 PTSY\n" + "LCH_NW 13329 LCH 13273 PTXM\n"
					+ "ICD9CM 40855 ICD9CM 20996 ABHTPT\n" + "MTHICD9 23525 ICD9CM 19120 ET\n"
					+ "USPMG 1703 USPMG 1696 HCPT\n" + "NCBI 1957848 NCBI 1409497 AUNCMNEQSCNSYUAUNUCNUEUSNUSY\n");

			ex.setDest("RSAB TFR\n" + "MTHMSTFRE 1833\n" + "MTHMSTITA 1799\n" + "ICPCITA 723\n" + "ICPCNOR 722\n"
					+ "CCS 1617\n" + "AOT 471\n" + "TKMT 770\n");

			exs.add(ex);

			examples = new SQLTableExamples(exs, query.getStatement());
			break;

		case "Problem 5":

			ex.setSource("MRRANK_RANK SAB TTY SUPPRESS\n" + "384 VANDF CD N\n" + "382 USP CD N\n" + "379 USPMG PT N\n"
					+ "378 DRUGBANK IN N\n" + "377 DRUGBANK SY N\n" + "376 DRUGBANK FSY N\n" + "375 MSH N1 N\n"
					+ "374 MSH PCE N\n" + "373 MSH CE N\n" + "372 FMA PT N\n" + "371 FMA SY N\n" + "370 FMA AB Y\n"
					+ "369 FMA OP Y\n" + "368 FMA IS Y\n" + "367 UWDA PT N\n" + "411 RXNORM DF N\n"
					+ "410 RXNORM SBDC N\n" + "409 RXNORM BN N\n" + "408 RXNORM PIN N\n" + "407 RXNORM BPCK N\n"
					+ "406 RXNORM GPCK N\n" + "405 RXNORM SY N\n" + "404 RXNORM TMSY N\n" + "403 MSH MH N\n"
					+ "402 MSH TQ N\n" + "401 MSH PEP N\n" + "400 MSH ET N\n" + "399 MSH XQ N\n" + "398 MSH PXQ N\n"
					+ "397 MSH NM N\n" + "396 HPO PT N\n" + "395 HPO SY N\n" + "394 HPO ET N\n" + "393 HPO OP Y\n"
					+ "392 HPO IS Y\n" + "391 NCBI SCN N\n" + "390 MTHSPL MTH_RXN_DP N\n" + "389 MTHSPL DP N\n"
					+ "388 MTHSPL SU N\n" + "387 ATC RXN_PT N\n" + "386 ATC PT N\n" + "385 VANDF PT N\n"
					+ "383 VANDF IN N\n" + "381 USP IN N\n" + "380 USPMG HC N\n");

			ex.setDest("MRRANK_RANK SAB TTY SUPPRESS\n" + "384 VANDF CD N\n" + "382 USP CD N\n" + "379 USPMG PT N\n"
					+ "378 DRUGBANK IN N\n" + "377 DRUGBANK SY N\n" + "376 DRUGBANK FSY N\n" + "375 MSH N1 N\n"
					+ "374 MSH PCE N\n" + "373 MSH CE N\n" + "372 FMA PT N\n" + "371 FMA SY N\n" + "370 FMA AB Y\n"
					+ "369 FMA OP Y\n" + "368 FMA IS Y\n" + "367 UWDA PT N\n");

			exs.add(ex);

			ex = new SQLTableExample();

			ex.setSource("MRRANK_RANK SAB TTY SUPPRESS\n" + "345 CHV PT N\n" + "442 ICPC CD N\n" + "340 ICPC PS Y\n"
					+ "239 ICPC PC N\n" + "637 ICPC CD N\n" + "234 ICPC CO N\n" + "333 AOT PT N\n" + "232 AOT ET N\n"
					+ "230 HCPCS OM Y\n" + "229 HCPCS OAM Y\n" + "227 GO MTH_PT N\n" + "626 GO CD N\n"
					+ "446 PDQ SY N\n" + "444 MEDLINEPLUS PT N\n" + "443 SOP PT N\n" + "441 ICPC PT N\n"
					+ "380 ICPC CX N\n" + "381 ICPC CS Y\n" + "735 ICPC CC N\n" + "531 HCPCS OP Y\n" + "428 GO PT N\n");

			ex.setDest("MRRANK_RANK SAB TTY SUPPRESS\n" + "345 CHV PT N\n" + "442 ICPC CD N\n" + "340 ICPC PS Y\n"
					+ "239 ICPC PC N\n" + "637 ICPC CD N\n" + "234 ICPC CO N\n" + "333 AOT PT N\n" + "232 AOT ET N\n"
					+ "230 HCPCS OM Y\n" + "229 HCPCS OAM Y\n" + "227 GO MTH_PT N\n" + "626 GO CD N\n");

			exs.add(ex);

			examples = new SQLTableExamples(exs, query.getStatement());
			break;

		case "Problem 6":
			ex.setSource("MRRANK_RANK SAB TTY SUPPRESS\n" + "405 RXNORM SY N\n" + "396 HPO PT N\n" + "386 ATC PT N\n"
					+ "385 VANDF PT N\n" + "379 USPMG PT N\n" + "372 FMA PT N\n" + "367 UWDA PT N\n"
					+ "411 RXNORM DF N\n" + "410 RXNORM SBDC N\n" + "409 RXNORM BN N\n" + "408 RXNORM PIN N\n"
					+ "407 RXNORM BPCK N\n" + "406 RXNORM GPCK N\n" + "404 RXNORM TMSY N\n" + "403 MSH MH N\n"
					+ "402 MSH TQ N\n" + "401 MSH PEP N\n" + "400 MSH ET N\n" + "399 MSH XQ N\n" + "398 MSH PXQ N\n"
					+ "397 MSH NM N\n" + "395 HPO SY N\n" + "394 HPO ET N\n" + "393 HPO OP Y\n" + "392 HPO IS Y\n"
					+ "391 NCBI SCN N\n" + "390 MTHSPL MTH_RXN_DP N\n" + "389 MTHSPL DP N\n" + "388 MTHSPL SU N\n"
					+ "387 ATC RXN_PT N\n" + "384 VANDF CD N\n" + "383 VANDF IN N\n" + "382 USP CD N\n"
					+ "381 USP IN N\n" + "380 USPMG HC N\n" + "378 DRUGBANK IN N\n" + "377 DRUGBANK SY N\n"
					+ "376 DRUGBANK FSY N\n" + "375 MSH N1 N\n" + "374 MSH PCE N\n" + "373 MSH CE N\n"
					+ "371 FMA SY N\n" + "370 FMA AB Y\n" + "369 FMA OP Y\n" + "368 FMA IS Y\n");

			ex.setDest("MRRANK_RANK SAB TTY SUPPRESS\n" + "405 RXNORM SY N\n" + "396 HPO PT N\n" + "386 ATC PT N\n"
					+ "385 VANDF PT N\n" + "379 USPMG PT N\n" + "372 FMA PT N\n" + "367 UWDA PT N\n");

			exs.add(ex);

			ex = new SQLTableExample();

			ex.setSource("MRRANK_RANK SAB TTY SUPPRESS\n" + "411 NCI_CTRP PT N\n" + "510 NCI_CTRP SY N\n"
					+ "808 NCI_FDA PT N\n" + "607 NCI_FDA SY N\n" + "305 NCI_GENC PT N\n" + "502 NCI_CRCH PT N\n"
					+ "300 NCI_DICOM PT N\n" + "499 NCI_CDISC-GLOSS PT N\n" + "797 NCI_BRIDG PT N\n"
					+ "294 NCI_BioC PT N\n" + "292 NCI_CTCAE PT N\n" + "991 NCI_EDQM-HC PT N\n"
					+ "489 NCI_CTCAE_5 PT N\n" + "288 NCI_CTCAE_3 PT N\n" + "287 NCI_CTEP-SDC PT N\n"
					+ "886 NCI_CTEP-SDC SY N\n" + "284 NCI_JAX PT N\n" + "582 NCI_KEGG PT N\n" + "280 NCI_ICH PT N\n"
					+ "278 NCI_NCI-HGNC PT N\n" + "477 NCI_NCI-HGNC SY N\n" + "276 NCI_NCI-HL7 PT N\n"
					+ "674 NCI_UCUM PT N\n" + "371 NCI_NICHD PT N\n" + "409 NCI_CTRP DN N\n" + "306 NCI HD N\n"
					+ "104 NCI_GENC CA2 N\n" + "303 NCI_GENC CA3 N\n" + "301 NCI_CRCH SY N\n"
					+ "298 NCI_CDISC-GLOSS SY N\n" + "296 NCI_BRIDG SY N\n" + "695 NCI_RENI DN N\n" + "293 NCI CCN N\n"
					+ "290 NCI_EDQM-HC SY N\n" + "285 NCI CCS N\n" + "283 NCI_JAX SY N\n" + "281 NCI_ICH AB N\n"
					+ "179 NCI_NCI-HL7 AB N\n" + "275 NCI_UCUM AB N\n" + "273 NCI_KEGG AB N\n" + "272 NCI_KEGG SY N\n");

			ex.setDest("MRRANK_RANK SAB TTY SUPPRESS\n" + "411 NCI_CTRP PT N\n" + "510 NCI_CTRP SY N\n"
					+ "808 NCI_FDA PT N\n" + "607 NCI_FDA SY N\n" + "305 NCI_GENC PT N\n" + "502 NCI_CRCH PT N\n"
					+ "300 NCI_DICOM PT N\n" + "499 NCI_CDISC-GLOSS PT N\n" + "797 NCI_BRIDG PT N\n"
					+ "294 NCI_BioC PT N\n" + "292 NCI_CTCAE PT N\n" + "991 NCI_EDQM-HC PT N\n"
					+ "489 NCI_CTCAE_5 PT N\n" + "288 NCI_CTCAE_3 PT N\n" + "287 NCI_CTEP-SDC PT N\n"
					+ "886 NCI_CTEP-SDC SY N\n" + "284 NCI_JAX PT N\n" + "582 NCI_KEGG PT N\n" + "280 NCI_ICH PT N\n"
					+ "278 NCI_NCI-HGNC PT N\n" + "477 NCI_NCI-HGNC SY N\n" + "276 NCI_NCI-HL7 PT N\n"
					+ "674 NCI_UCUM PT N\n" + "371 NCI_NICHD PT N\n");

			exs.add(ex);

			examples = new SQLTableExamples(exs, query.getStatement());
			break;

		case "Problem 7":
			ex.setSource("VCUI RCUI VSAB RSAB SON SF SVER\n" + "C1140092 C1140091 AIR93 AIR AI/RHEUM_1993 AIR 1993\n"
					+ "C1140098 C1140097 CST95 CST COSTART_1995 CST 1995\n"
					+ "C1140099 C1140100 DXP94 DXP DXplain_1994 DXP 1994\n"
					+ "C1140105 C1140106 LCH90 LCH Library_of_Congress_Subject_Headings_1990 LCH 1990\n"
					+ "C1140107 C1140108 MCM92 MCM McMaster_University_Epidemiology_Terms_1992 MCM 1992\n"
					+ "C1140146 C1140145 ICPC93 ICPC International_Classification_of_Primary_Care_1993 ICPC 1993\n"
					+ "C1140152 C1140153 QMR96 QMR Quick_Medical_Reference_(QMR)_1996 QMR 1996\n"
					+ "C1140163 C1140162 AOD2000 AOD Alcohol_and_Other_Drug_Thesaurus_2000 AOD 2000\n"
					+ "C1140190 C1140191 ICPCDAN_1993 ICPCDAN ICPC_Danish_Translation_1993 ICPC 1993\n"
					+ "C1140193 C1140192 ICPCDUT_1993 ICPCDUT ICPC_Dutch_Translation_1993 ICPC 1993\n"
					+ "C1140195 C1140194 ICPCFIN_1993 ICPCFIN ICPC_Finnish_Translation_1993 ICPC 1993\n"
					+ "C1140197 C1140196 ICPCFRE_1993 ICPCFRE ICPC_French_Translation_1993 ICPC 1993\n"
					+ "C1140199 C1140198 ICPCGER_1993 ICPCGER ICPC_German_Translation_1993 ICPC 1993\n"
					+ "C1140201 C1140200 ICPCHUN_1993 ICPCHUN ICPC_Hungarian_Translation_1993 ICPC 1993\n"
					+ "C1140204 C1140205 ICPCPOR_1993 ICPCPOR ICPC_Portuguese_Translation_1993 ICPC 1993\n"
					+ "C1140206 C1140207 ICPCSPA_1993 ICPCSPA ICPC_Spanish_Translation_1993 ICPC 1993\n"
					+ "C1140208 C1140209 ICPCSWE_1993 ICPCSWE ICPC_Swedish_Translation_1993 ICPC 1993\n"
					+ "C1140223 C1140222 RAM99 RAM QMR_clinically_related_terms_from_Randolph_A._Miller_1999 RAM 1999\n"
					+ "C1140240 C1140241 ICPCBAQ_1993 ICPCBAQ ICPC_Basque_Translation_1993 ICPC 1993\n"
					+ "C1140242 C1140243 ICPCHEB_1993 ICPCHEB ICPC_Hebrew_Translation_1993 ICPC 1993\n"
					+ "C1140244 C1140245 NCISEER_1999 NCISEER NCI_SEER_ICD_Neoplasm_Code_Mappings_1999 NCISEER 1999\n"
					+ "C1140291 C1140292 MTHMST2001 MTHMST Metathesaurus_Version_of_Minimal_Standard_Terminology_Digestive_Endoscopy_2001 MTHMST 2001\n"
					+ "C1140293 C1140294 MTHMSTFRE_2001 MTHMSTFRE Metathesaurus_Version_of_Minimal_Standard_Terminology_Digestive_Endoscopy_French_Translation_2001 MTHMST 2001\n"
					+ "C1140296 C1140295 MTHMSTITA_2001 MTHMSTITA Metathesaurus_Version_of_Minimal_Standard_Terminology_Digestive_Endoscopy_Italian_Translation_2001 MTHMST 2001\n");

			ex.setDest("SVER\n" + "1993\n" + "1995\n" + "1994\n" + "1990\n" + "1992\n");

			exs.add(ex);

			ex = new SQLTableExample();

			ex.setSource("VCUI RCUI VSAB RSAB SON SF SVER\n"
					+ "C4743534 C3826810 NCI2018_NCPDP_1812 NCI_NCPDP National_Council_for_Prescription_Drug_Programs_1812 NCI 1812\n"
					+ "C4743535 C3826812 NCI2018_KEGG_1812 NCI_KEGG KEGG_Pathway_Database_1812 NCI 1812\n"
					+ "C4743536 C3826820 NCI2018_CDISC_1812 NCI_CDISC Clinical_Data_Interchange_Standards_Consortium_1812 NCI 1812\n"
					+ "C4743537 C4329208 NCI2018_NCI-HGNC_1812 NCI_NCI-HGNC NCI_HUGO_Gene_Nomenclature_1812 NCI 1812\n"
					+ "C4743538 C4552919 NCI2018_CTRP_1812 NCI_CTRP Clinical_Trials_Reporting_Program_1812 NCI 1812\n"
					+ "C4743539 C4553192 NCI2018_CDISC-GLOSS_1812 NCI_CDISC-GLOSS CDISC_Glossary_Terminology_1812 NCI 1812\n"
					+ "C4743540 C4743543 NCI2018_CPTAC_1812 NCI_CPTAC Clinical_Proteomic_Tumor_Analysis_Consortium_Terms_1812 NCI 1812\n"
					+ "C4743541 C4743542 NCI2018_INC_1812 NCI_INC International_Neonatal_Consortium_Terms_1812 NCI 1812\n"
					+ "C4746242 C4074772 LNC-DE-AT_265 LNC-DE-AT LOINC_German_Austria_Edition_265 LNC 1065\n"
					+ "C4746243 C4047278 LNC-DE-CH_265 LNC-DE-CH LOINC_German_Switzerland_Edition_265 LNC 2265\n"
					+ "C4746244 C4047277 LNC-DE-DE_265 LNC-DE-DE LOINC_German_Germany_Edition_265 LNC 2650\n"
					+ "C4746245 C4047276 LNC-EL-GR_265 LNC-EL-GR LOINC_Greek_Greece_Edition_265 LNC 1065\n");

			ex.setDest("SVER\n" + "1812\n" + "1065\n");

			exs.add(ex);

			examples = new SQLTableExamples(exs, query.getStatement());
			break;

		case "Problem 8":
			ex.setSource("CUI TUI STN\n" + "C0000005 T130 A1.4.1.1.4\n" + "C0000039 T109 A1.4.1.2.1\n"
					+ "C0000039 T121 A1.4.1.1.1\n" + "C0000052 T116 A1.4.1.2.1.7\n" + "C0000052 T126 A1.4.1.1.3.3\n"
					+ "C0000074 T109 A1.4.1.2.1\n" + "C0000084 T116 A1.4.1.2.1.7\n" + "C0000084 T123 A1.4.1.1.3\n"
					+ "C0000096 T109 A1.4.1.2.1\n" + "C0000096 T121 A1.4.1.1.1\n" + "C0000097 T109 A1.4.1.2.1\n"
					+ "C0000097 T131 A1.4.1.1.5\n" + "C0000098 T109 A1.4.1.2.1\n" + "C0000098 T131 A1.4.1.1.5\n"
					+ "C0000102 T109 A1.4.1.2.1\n" + "C0000102 T131 A1.4.1.1.5\n");

			ex.setDest("CUI TUI STN\n" + "C0000097 T131 A1.4.1.1.5\n" + "C0000098 T131 A1.4.1.1.5\n"
					+ "C0000102 T131 A1.4.1.1.5\n" + "C0000005 T130 A1.4.1.1.4\n" + "C0000052 T126 A1.4.1.1.3.3\n"
					+ "C0000084 T123 A1.4.1.1.3\n" + "C0000039 T121 A1.4.1.1.1\n" + "C0000096 T121 A1.4.1.1.1\n"
					+ "C0000052 T116 A1.4.1.2.1.7\n" + "C0000084 T116 A1.4.1.2.1.7\n" + "C0000039 T109 A1.4.1.2.1\n"
					+ "C0000074 T109 A1.4.1.2.1\n" + "C0000096 T109 A1.4.1.2.1\n" + "C0000097 T109 A1.4.1.2.1\n"
					+ "C0000098 T109 A1.4.1.2.1\n" + "C0000102 T109 A1.4.1.2.1\n");

			exs.add(ex);

			ex = new SQLTableExample();

			ex.setSource("CUI TUI STN\n" + "C0000855 T121 A1.4.1.1.1\n" + "C0000855 T197 A1.4.1.2.2\n"
					+ "C0000857 T057 B1.3\n" + "C0000858 T057 B1.3\n" + "C0000863 T083 A2.1.5.4\n"
					+ "C0000864 T055 B1.1.2\n" + "C0000866 T074 A1.3.1\n" + "C0000869 T002 A1.1.3.3\n"
					+ "C0000872 T073 A1.3\n" + "C0000872 T093 A2.7.1\n" + "C0000873 T033 A2.2\n"
					+ "C0000875 T065 B1.3.4\n" + "C0000876 T092 A2.7\n" + "C0000877 T092 A2.7\n"
					+ "C0000879 T204 A1.1.3\n" + "C0000880 T047 B2.2.1.2.1\n" + "C0000881 T204 A1.1.3\n"
					+ "C0000886 T025 A1.2.3.3\n" + "C0000887 T046 B2.2.1.2\n" + "C0000889 T047 B2.2.1.2.1\n"
					+ "C0000890 T204 A1.1.3\n" + "C0000892 T204 A1.1.3\n" + "C0000893 T196 A1.4.1.2.3\n"
					+ "C0000894 T067 B2\n" + "C0000895 T060 B1.3.1.2\n" + "C0000898 T080 A2.1.2\n"
					+ "C0000899 T055 B1.1.2\n" + "C0000901 T098 A2.9.2\n" + "C0000905 T023 A1.2.3.1\n");

			ex.setDest("CUI TUI STN\n" + "C0000879 T204 A1.1.3\n" + "C0000881 T204 A1.1.3\n" + "C0000890 T204 A1.1.3\n"
					+ "C0000892 T204 A1.1.3\n" + "C0000855 T197 A1.4.1.2.2\n" + "C0000893 T196 A1.4.1.2.3\n"
					+ "C0000855 T121 A1.4.1.1.1\n" + "C0000901 T098 A2.9.2\n" + "C0000872 T093 A2.7.1\n"
					+ "C0000876 T092 A2.7\n" + "C0000877 T092 A2.7\n" + "C0000863 T083 A2.1.5.4\n"
					+ "C0000898 T080 A2.1.2\n" + "C0000866 T074 A1.3.1\n" + "C0000872 T073 A1.3\n"
					+ "C0000894 T067 B2\n" + "C0000875 T065 B1.3.4\n" + "C0000895 T060 B1.3.1.2\n"
					+ "C0000857 T057 B1.3\n" + "C0000858 T057 B1.3\n" + "C0000864 T055 B1.1.2\n"
					+ "C0000899 T055 B1.1.2\n" + "C0000880 T047 B2.2.1.2.1\n" + "C0000889 T047 B2.2.1.2.1\n"
					+ "C0000887 T046 B2.2.1.2\n" + "C0000873 T033 A2.2\n" + "C0000886 T025 A1.2.3.3\n"
					+ "C0000905 T023 A1.2.3.1\n" + "C0000869 T002 A1.1.3.3\n");

			exs.add(ex);

			examples = new SQLTableExamples(exs, query.getStatement());
			break;

		default:
			return null;
		}

		return ctrl.repairAQuery(examples);

	}

}
