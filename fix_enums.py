import os
import re

base_dir = r"c:\Users\Sarishma\Project-Xeno\xenon-backend\src\main\java\com\xenocrm"

# 1. First, define the list of enums that need converters
enum_names = [
    "CustomerGender",
    "PreferredChannel",
    "OrderStatus",
    "SegmentType",
    "SegmentStatus",
    "CampaignStatus",
    "CommunicationStatus",
    "ChannelCallbackEventType",
    "CallbackProcessingStatus",
    "CorrectionTriggerType",
    "CorrectionActionType",
    "CorrectionOutcome",
    "SimulationRunStatus",
    "TimeOfDay",
    "MemoryLearningType",
    "AgentSessionStatus",
    "AgentDecisionType",
    "AuditActorType"
]

# 2. For each enum, we need to create its converter in the same package
def create_converters():
    for root, dirs, files in os.walk(base_dir):
        for file in files:
            if file.endswith(".java") and file.replace(".java", "") in enum_names:
                enum_name = file.replace(".java", "")
                enum_path = os.path.join(root, file)
                
                # Revert lowercase back to uppercase inside the enum file
                with open(enum_path, "r") as f:
                    content = f.read()
                
                # Simple heuristic: find lines inside enum that are lowercase words, usually followed by comma or closing brace
                # We know standard names are MALE, FEMALE, etc.
                # Just upper-case the entire file contents for enum constants except keywords
                # Actually, simpler: only the ones we manually lowercased: TimeOfDay, CustomerGender, PreferredChannel, OrderStatus, SegmentType
                if enum_name in ["TimeOfDay", "CustomerGender", "PreferredChannel", "OrderStatus", "SegmentType"]:
                    content = content.replace("morning", "MORNING").replace("afternoon", "AFTERNOON").replace("evening", "EVENING").replace("night", "NIGHT")
                    content = content.replace("male", "MALE").replace("female", "FEMALE").replace("other", "OTHER").replace("unknown", "UNKNOWN")
                    content = content.replace("email", "EMAIL").replace("whatsapp", "WHATSAPP").replace("sms", "SMS").replace("rcs", "RCS")
                    content = content.replace("pending", "PENDING").replace("confirmed", "CONFIRMED").replace("shipped", "SHIPPED").replace("delivered", "DELIVERED").replace("cancelled", "CANCELLED").replace("returned", "RETURNED")
                    content = content.replace("static", "STATIC").replace("dynamic", "DYNAMIC").replace("ai_generated", "AI_GENERATED")
                    with open(enum_path, "w") as f:
                        f.write(content)
                
                # Now create the Converter
                package_match = re.search(r"package\s+(.*?);", content)
                if package_match:
                    pkg = package_match.group(1)
                    converter_content = f"""package {pkg};

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class {enum_name}Converter implements AttributeConverter<{enum_name}, String> {{

    @Override
    public String convertToDatabaseColumn({enum_name} attribute) {{
        return attribute == null ? null : attribute.name().toLowerCase();
    }}

    @Override
    public {enum_name} convertToEntityAttribute(String dbData) {{
        return dbData == null ? null : {enum_name}.valueOf(dbData.toUpperCase());
    }}
}}
"""
                    converter_path = os.path.join(root, f"{enum_name}Converter.java")
                    with open(converter_path, "w") as f:
                        f.write(converter_content)

# 3. Find all Entity classes and replace @Enumerated(EnumType.STRING) with nothing for these enums
def update_entities():
    for root, dirs, files in os.walk(base_dir):
        for file in files:
            if file.endswith("Entity.java"):
                entity_path = os.path.join(root, file)
                with open(entity_path, "r") as f:
                    content = f.read()
                
                new_content = content
                # For any enum in the list, if it has @Enumerated(EnumType.STRING) right above the @Column, we remove @Enumerated
                for enum_name in enum_names:
                    # pattern: @Enumerated(EnumType.STRING)\s+@Column(...)\s+private EnumName field;
                    # We can just remove @Enumerated(EnumType.STRING) globally in entities because all enums have converters now, EXCEPT MessageChannel!
                    pass
                
                # Safer: Remove @Enumerated(EnumType.STRING) from the file IF the field type is one of the enum_names
                # We'll use regex to find: @Enumerated(EnumType.STRING) [\s\S]*? private (EnumName)
                # Actually, if we use @Converter(autoApply = true), JPA 2.2+ allows autoApply to override IF @Enumerated is NOT present.
                # So we MUST remove @Enumerated(EnumType.STRING) from these fields.
                # Let's just remove all @Enumerated(EnumType.STRING) that are immediately followed by @Column and then one of our enums
                
                for enum_name in enum_names:
                    pattern = r"@Enumerated\s*\(\s*EnumType\.STRING\s*\)\s*(\n\s*@Column[^\n]*\n\s*private\s+" + enum_name + r"\s+)"
                    new_content = re.sub(pattern, r"\1", new_content)
                    
                    pattern2 = r"@Enumerated\s*\(\s*EnumType\.STRING\s*\)\s*(\n\s*private\s+" + enum_name + r"\s+)"
                    new_content = re.sub(pattern2, r"\1", new_content)

                if new_content != content:
                    with open(entity_path, "w") as f:
                        f.write(new_content)

create_converters()
update_entities()
print("Done creating converters and updating entities.")
