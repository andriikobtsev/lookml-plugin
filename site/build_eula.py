#!/usr/bin/env python3
"""Generate site/eula.html from the canonical eula.md. Run during Pages deploy;
the output is not committed (single source of truth is eula.md)."""
import pathlib
import markdown

ROOT = pathlib.Path(__file__).resolve().parent.parent
body = markdown.markdown(
    (ROOT / "eula.md").read_text(encoding="utf-8"),
    extensions=["extra", "sane_lists"],
)

PAGE = """<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>End User License Agreement - LookML Support</title>
  <meta name="description" content="End User License Agreement for the LookML Support plugin.">
  <style>
    :root {{ --bg:#0e1116; --border:#2a313c; --text:#e6edf3; --muted:#9aa7b4; --accent:#4f8cff; }}
    * {{ box-sizing:border-box; }}
    body {{ margin:0; font:16px/1.7 -apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,Helvetica,Arial,sans-serif; background:var(--bg); color:var(--text); }}
    a {{ color:var(--accent); }}
    .wrap {{ max-width:760px; margin:0 auto; padding:48px 20px 80px; }}
    h1 {{ font-size:1.9rem; }}
    h2 {{ font-size:1.2rem; margin-top:32px; border-top:1px solid var(--border); padding-top:24px; }}
    .back {{ display:inline-block; margin-bottom:24px; }}
  </style>
</head>
<body>
  <div class="wrap">
    <a class="back" href="./index.html">\u2190 LookML Support</a>
{body}
  </div>
</body>
</html>
"""

out = ROOT / "site" / "eula.html"
out.write_text(PAGE.format(body=body), encoding="utf-8")
print(f"Generated {out.relative_to(ROOT)}")
