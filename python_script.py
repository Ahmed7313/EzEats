import os
import math
from xml.etree.ElementTree import Element, SubElement, ElementTree, parse

# Define range and sizes
dp_range = range(-1000, 1001)  # -1000dp to 1000dp

# Define screen size buckets for various device configurations
screen_sizes = {
    "values-sw300dp": 1.0,
    "values-sw330dp": 1.1,
    "values-sw360dp": 1.2,
    "values-sw400dp": 1.33,
    "values-sw420dp": 1.4,
    "values-sw480dp": 1.6,
    "values-sw600dp": 2.0,
    "values-sw720dp": 2.4,
    "values-sw800dp": 2.67,
    "values-sw1024dp": 3.41
}

# project_directory = r'C:\Users\pc\AndroidStudioProjects\EzEats2'  # Adjust this to your project path


# Directory for dimens files
# res_directory = "D:/Work/Modon%20Mobile%20App%20Android - Copy/app/src/main/res"
res_directory = r'C:\Users\pc\AndroidStudioProjects\EzEats2'


def create_dimens_file(directory, dp_values, scale):
    """Creates dimens.xml file in the specified directory with scaled dp values."""
    os.makedirs(directory, exist_ok=True)  # Ensure the directory exists
    root = Element('resources')

    # Iterate through dp range and create scaled values
    for dp in dp_values:
        name = f"_{dp}dp" if dp >= 0 else f"_minus{-dp}dp"
        dimen = SubElement(root, 'dimen', name=name)
        # Apply scaling and ensure minimum positive value is 1 for positive values
        scaled_value = max(1, math.floor(dp * scale)) if dp > 0 else round(dp * scale, 2)
        dimen.text = f"{scaled_value}dp"

    # Write the XML structure to the dimens.xml file
    tree = ElementTree(root)
    with open(os.path.join(directory, 'dimens.xml'), 'wb') as f:
        tree.write(f, encoding='utf-8', xml_declaration=True)


# Ensure all dimensions are included in the base values folder
def update_base_dimens_file(base_directory, dp_values):
    """Updates the base values folder with any missing dimensions."""
    base_file = os.path.join(base_directory, "dimens.xml")
    os.makedirs(base_directory, exist_ok=True)  # Ensure the base directory exists

    if os.path.exists(base_file):
        # Parse existing dimens.xml file if it exists
        tree = parse(base_file)
        root = tree.getroot()
        existing_names = {dimen.get("name") for dimen in root.findall("dimen")}
    else:
        root = Element('resources')
        existing_names = set()

    # Add missing dimensions to the base dimens.xml
    for dp in dp_values:
        name = f"_{dp}dp" if dp >= 0 else f"_minus{-dp}dp"
        if name not in existing_names:
            dimen = SubElement(root, 'dimen', name=name)
            dimen.text = f"{dp}dp"

    # Write updated XML back to the base file
    tree = ElementTree(root)
    with open(base_file, 'wb') as f:
        tree.write(f, encoding='utf-8', xml_declaration=True)


# Update the base dimens file first
update_base_dimens_file(f"{res_directory}/values", dp_range)

# Create dimens files for all screen sizes
for directory, scale in screen_sizes.items():
    create_dimens_file(f"{res_directory}/{directory}", dp_range, scale)

print("Dimens files created and base dimens file updated.")

# Script 2: Update project files to use dimens
import os
import re

project_root = "D:/Work/Modon%20Mobile%20App%20Android - Copy/app/src/main"  # Root directory of your project


def adjust_dimension(match, scale_dp):
    """Adjusts a dimension based on type and scale."""
    value, unit = match.groups()
    value = int(value)
    if value == 0:
        return "0dp"  # Ensure 0dp remains unchanged
    elif unit in ("dp", "sp"):
        if value > 0:
            adjusted_value = max(1, math.floor(value / scale_dp))
        else:
            adjusted_value = math.ceil(value / scale_dp)  # Use ceil for negative scaling
    else:  # sdp or ssp
        adjusted_value = value

    if adjusted_value < 0:
        return f"@dimen/_minus{abs(adjusted_value)}dp"  # Correct negative formatting
    else:
        return f"@dimen/_{adjusted_value}dp"


def should_skip_file(file_path):
    return "dimens.xml" in file_path


def update_xml_files():
    scale_dp = 1.3
    for root, _, files in os.walk(project_root):
        for file in files:
            if file.endswith('.xml'):
                file_path = os.path.join(root, file)
                if should_skip_file(file_path):
                    continue

                with open(file_path, 'r', encoding='utf-8') as f:
                    content = f.read()

                content = re.sub(r'\b(-?\d+)(dp|sp)\b', lambda m: adjust_dimension(m, scale_dp),
                                 content)

                # Correct existing incorrect negative dimension references
                content = re.sub(r'android:(.*?)"-@dimen/(_minus\d+dp)"', r'android:\1"@dimen/\2"',
                                 content)
                content = re.sub(r'layout_(.*?)"-@dimen/(minus\d+dp)"', r'layout\1"@dimen/\2"',
                                 content)
                content = re.sub(r'tools:(.*?)"-@dimen/(_minus\d+dp)"', r'tools:\1"@dimen/\2"',
                                 content)

                patterns = [
                    (r'@dimen/(-?\d+)sdp', r'@dimen/_\1dp'),
                    (r'@dimen/(-?\d+)sdp', r'@dimen/\1dp'),
                    (r'@dimen/(-?\d+)ssp', r'@dimen/_\1dp'),
                    (r'@dimen/(-?\d+)ssp', r'@dimen/\1dp')
                ]

                for pattern, replacement in patterns:
                    content = re.sub(pattern, replacement, content)

                remaining_sdp_ssp = re.findall(r'@dimen/[^ ]*sdp|@dimen/[^ ]*ssp', content)
                if remaining_sdp_ssp:
                    print(f"Unprocessed sdp/ssp dimensions: {remaining_sdp_ssp} in {file_path}")

                content = re.sub(r'@\{R\.dimen\.(_?-?\d+sdp)\}',
                                 lambda m: m.group(0).replace("sdp", "dp"), content)

                with open(file_path, 'w', encoding='utf-8') as f:
                    f.write(content)


# Function to update Kotlin files in the project
def update_kotlin_files():
    scale_dp = 1.3  # Scale factor for dp and sp
    for root, _, files in os.walk(project_root):
        for file in files:
            if file.endswith('.kt'):
                file_path = os.path.join(root, file)
                with open(file_path, 'r', encoding='utf-8') as f:
                    content = f.read()

                # Replace hardcoded dimensions in Kotlin files
                patterns = [
                    (r'com\.intuit\.sdp\.R\.dimen\.(-?\d+)sdp', r"R.dimen._\1dp"),
                    (r'com\.intuit\.sdp\.R\.dimen\.(-?\d+)sdp', r"R.dimen.\1dp"),
                    (r'com\.intuit\.sdp\.R\.dimen\.(-?\d+)ssp', r"R.dimen._\1dp"),
                    (r'com\.intuit\.sdp\.R\.dimen\.(-?\d+)ssp', r"R.dimen.\1dp")
                ]

                for pattern, replacement in patterns:
                    content = re.sub(pattern, replacement, content)

                with open(file_path, 'w', encoding='utf-8') as f:
                    f.write(content)


# Run updates for XML and Kotlin files
update_xml_files()
update_kotlin_files()

print("Project files updated to use dimens.")