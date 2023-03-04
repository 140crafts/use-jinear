import Logger from "./logger";

const logger = Logger("url-helper");

export const isUrl = (word: string) => {
  try {
    new URL(word);
    logger.log(`[URL] ${word}`);
    return true;
  } catch (e) {}
  logger.log(`[WORD] ${word}`);
  return false;
};

export const urlify = (text: string) => {
  logger.log({ func: "urlify", text });
  const parser = new DOMParser();
  const htmlDoc = parser.parseFromString(text, "text/html");
  const nodes = htmlDoc.body.childNodes;
  for (let i = 0; i < nodes.length; i++) {
    const node = nodes[i];
    const nodeVal = node.nodeValue;
    const nodeName = node.nodeName;
    if (nodeName == "#text" && nodeVal) {
      const parentDiv = createDivElement({ document });
      nodeVal.split(" ").map((word) => {
        logger.log({ func: "urlify", word });
        const child = isUrl(word)
          ? createAnchorElement({
              document,
              href: word.replace(" ", ""),
              text: word,
            })
          : createDivElement({ document, text: word });
        parentDiv.appendChild(child);
      });
      htmlDoc.body.replaceChild(parentDiv, node);
    }
  }
  logger.log({ func: "urlify", bodyNodes: htmlDoc.body.childNodes });
  return new XMLSerializer().serializeToString(htmlDoc);
};

export const createAnchorElement = (vo: { document: Document; href: string; text: string }) => {
  const containerDiv = createDivElement({ document });
  const anchorElement = document.createElement("a");
  anchorElement.href = vo.href;
  anchorElement.text = vo.text;
  anchorElement.target = "_blank";
  containerDiv.appendChild(anchorElement);
  return containerDiv;
};

export const createDivElement = (vo: { document: Document; text?: string }) => {
  const el = document.createElement("div");
  if (vo.text) {
    el.innerText = " " + vo.text + " ";
  }
  return el;
};
