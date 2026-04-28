#!/usr/bin/env python3
"""
Generate a bundled IntelliJ live template XML file.

Example:
  ./scripts/scaffold_live_template.py \
    --group Markdown \
    --name "{" \
    --description "Insert Markdown link" \
    --value '[$TEXT$]($LINK$)$END$' \
    --context MARKDOWN \
    --variable 'TEXT|||true' \
    --variable 'LINK|complete()||true' \
    --output src/main/resources/liveTemplates/Markdown.xml
"""

from __future__ import annotations

import argparse
import sys
import xml.etree.ElementTree as ET
from pathlib import Path


def parse_bool(value: str) -> str:
    lowered = value.lower()
    if lowered not in {"true", "false"}:
        raise argparse.ArgumentTypeError("expected true or false")
    return lowered


def parse_variable(spec: str) -> dict[str, str]:
    parts = spec.split("|")
    if not parts or not parts[0].strip():
        raise argparse.ArgumentTypeError(
            "variable spec must start with NAME, format: NAME|expression|defaultValue|alwaysStopAt"
        )

    while len(parts) < 4:
        parts.append("")

    always_stop_at = parts[3].strip() or "true"
    if always_stop_at.lower() not in {"true", "false"}:
        raise argparse.ArgumentTypeError(
            "alwaysStopAt must be true or false in variable spec"
        )

    return {
        "name": parts[0].strip(),
        "expression": parts[1].strip(),
        "defaultValue": parts[2].strip(),
        "alwaysStopAt": always_stop_at.lower(),
    }


def build_xml(args: argparse.Namespace) -> str:
    template_set = ET.Element("templateSet", {"group": args.group})
    template_attrs = {
        "name": args.name,
        "value": args.value,
        "toReformat": args.to_reformat,
        "toShortenFQNames": args.to_shorten_fq_names,
        "useStaticImport": args.use_static_import,
        "shortcut": args.shortcut,
    }
    if args.description:
        template_attrs["description"] = args.description
    if args.deactivated:
        template_attrs["deactivated"] = "true"
    template = ET.SubElement(template_set, "template", template_attrs)

    for spec in args.variable:
        ET.SubElement(template, "variable", parse_variable(spec))

    if args.context:
        context = ET.SubElement(template, "context")
        for context_name in args.context:
            ET.SubElement(context, "option", {"name": context_name, "value": "true"})

    tree = ET.ElementTree(template_set)
    ET.indent(tree, space="  ")

    from io import StringIO

    buffer = StringIO()
    tree.write(buffer, encoding="unicode", xml_declaration=False)
    return buffer.getvalue().strip() + "\n"


def main() -> int:
    parser = argparse.ArgumentParser(description="Generate IntelliJ live template XML")
    parser.add_argument("--group", required=True, help="templateSet group name")
    parser.add_argument("--name", required=True, help="template abbreviation")
    parser.add_argument("--value", required=True, help="template value")
    parser.add_argument("--description", default="", help="template description")
    parser.add_argument(
        "--shortcut",
        default="TAB",
        choices=["TAB", "ENTER", "SPACE", "NONE"],
        help="expansion shortcut",
    )
    parser.add_argument(
        "--context",
        action="append",
        default=[],
        help="context option name, repeatable",
    )
    parser.add_argument(
        "--variable",
        action="append",
        default=[],
        help="NAME|expression|defaultValue|alwaysStopAt, repeatable",
    )
    parser.add_argument(
        "--to-reformat",
        default="false",
        type=parse_bool,
        help="set to true to reformat generated code",
    )
    parser.add_argument(
        "--to-shorten-fq-names",
        default="false",
        type=parse_bool,
        help="set to true to shorten fully qualified names",
    )
    parser.add_argument(
        "--use-static-import",
        default="false",
        type=parse_bool,
        help="set to true to allow static imports",
    )
    parser.add_argument(
        "--deactivated",
        action="store_true",
        help="mark the bundled template as deactivated",
    )
    parser.add_argument("--output", help="write XML to a file instead of stdout")
    args = parser.parse_args()

    xml_text = build_xml(args)
    if args.output:
        output = Path(args.output)
        output.parent.mkdir(parents=True, exist_ok=True)
        output.write_text(xml_text, encoding="utf-8")
    else:
        sys.stdout.write(xml_text)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
