import utils.FileReaderTools;

public class Main {
    public static void main(String[] args) {
//        String fileName = "resources\\test1";
        String fileName = "resources\\Puzzle Input";
        String[] inputLines = FileReaderTools.readFileAsArray(fileName);

        int partNumberTotal = getPartNumberTotal(inputLines);

        System.out.println("Total: " + partNumberTotal);
    }

    private static int getPartNumberTotal(String[] schematicLines) {
        int total = 0;
        for(int i=0; i<schematicLines.length; i++) {
            total += getLinePartNumbers(i, schematicLines);
        }
        return total;
    }

    private static int getLinePartNumbers(int lineNum, String[] allLines) {
        int lineTotal = 0;
        String line = allLines[lineNum];
        for (int charIdx=0; charIdx<line.length(); charIdx++) {
            if (Character.isDigit(line.charAt(charIdx))) {
                StringBuilder partNumBuilder = new StringBuilder();
                int idx = charIdx;
                do {
                    partNumBuilder.append(line.charAt(idx));
                    idx++;
                } while (idx < line.length() && Character.isDigit(line.charAt(idx)));

                String partNum = partNumBuilder.toString();
                if (isPartNumber(partNum, charIdx, lineNum, allLines)) {
                    lineTotal += Integer.parseInt(partNum);
                    System.out.println(partNum);
                }
                charIdx += partNum.length()-1;
            }
        }

        return lineTotal;
    }

    private static boolean isPartNumber(String partNum, int numIdx, int lineNum, String[] lines) {
        // Scan for symbols all around the part number String
        boolean up = scanUp(partNum, numIdx, lineNum, lines);
        boolean down = scanDown(partNum, numIdx, lineNum, lines);
        boolean left = scanLeft(numIdx, lines[lineNum]);
        boolean right = scanRight(numIdx, partNum, lines[lineNum]);
        return up || down || left || right;
    }

    private static boolean scanUp(String partNum, int numIdx, int lineNum, String[] lines) {
        // Scan the string above the potential part number
        if (lineNum == 0) return false;

        String scanString = lines[lineNum-1].substring(
                Math.max(0, numIdx-1),
                Math.min(lines[lineNum].length(), numIdx+partNum.length()+1)
        );
        return scanForSymbol(scanString);
    }

    private static boolean scanDown(String partNum, int numIdx, int lineNum, String[] lines) {
        // Scan the string below the potential part number
        if (lineNum == lines.length-1) return false;

        String scanString = lines[lineNum+1].substring(
                Math.max(0, numIdx-1),
                Math.min(lines[lineNum].length(), numIdx+partNum.length()+1)
        );
        return scanForSymbol(scanString);
    }

    private static boolean scanForSymbol(String scanString) {
        for (int i = 0; i< scanString.length(); i++) {
            char scanChar = scanString.charAt(i);
            if (scanChar != '.' && !Character.isDigit(scanChar)) return true;
        }

        return false;
    }

    private static boolean scanRight(int numIdx, String partNum, String line) {
        int endIdx = numIdx + partNum.length()-1;
        if (endIdx == line.length()-1) return false;
        return scanForSymbol(line.substring(endIdx+1, endIdx+2));
    }

    private static boolean scanLeft(int numIdx, String line) {
        if (numIdx == 0) return false;
        return scanForSymbol(line.substring(numIdx-1, numIdx));
    }
}