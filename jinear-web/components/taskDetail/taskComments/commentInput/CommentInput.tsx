import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import useWindowSize from "@/hooks/useWindowSize";
import { CommentDto } from "@/model/be/jinear-core";
import { useInitializeTaskCommentMutation } from "@/store/api/taskCommentApi";
import { selectCurrentAccount } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { getOffset, getSize } from "@/utils/htmlUtis";
import Logger from "@/utils/logger";
import useTranslation from "locales/useTranslation";
import React, { Dispatch, KeyboardEvent, SetStateAction, useEffect, useRef, useState } from "react";
import { FaBold, FaHeading, FaItalic } from "react-icons/fa";
import { IoClose } from "react-icons/io5";
import CommentSimple from "../commentSimple/CommentSimple";
import styles from "./CommentInput.module.scss";

interface CommentInputProps {
  taskId: string;
  quotedComment?: CommentDto;
  setQuotedComment?: Dispatch<SetStateAction<CommentDto | undefined>>;
}

const ALLOWED_TAGS = ["h1", "p", "b", "i", "em", "strong", "a", "br", "li", "ul", "ol", "blockquote", "br"];

const SANITIZE_CONFIG = {
  allowedTags: ALLOWED_TAGS,
  allowedAttributes: {
    a: ["href", "target"],
  },
};

const logger = Logger("CommentInput");
const CommentInput: React.FC<CommentInputProps> = ({ taskId, quotedComment, setQuotedComment }) => {
  const { t } = useTranslation();
  const currentAccount = useTypedSelector(selectCurrentAccount);
  const { width } = useWindowSize();
  const [toolbarStyle, setToolbarStyle] = useState<any>({});
  const inputRef = useRef<HTMLDivElement>(null);
  const [renderPlaceholder, setRenderPlaceholder] = useState<boolean>(true);
  const [initializeTaskComment, { isLoading, isSuccess }] = useInitializeTaskCommentMutation();

  useEffect(() => {
    if (inputRef.current && isSuccess) {
      inputRef.current.innerHTML = "";
      setRenderPlaceholder(true);
      setQuotedComment?.(undefined);
      setToolbarStyle({});
    }
  }, [isSuccess]);

  const onTextSelect = () => {
    const s = window.getSelection();
    const oRange = s?.getRangeAt(0); //get the text range
    const oRect = oRange?.getBoundingClientRect();
    // logger.log({ s: s?.toString(), oRange: oRange?.toString(), oRect: oRect?.toJSON(), left: oRect?.left, width });
    if (oRange?.toString() != "") {
      const inputOffset: any = inputRef.current ? getOffset(inputRef.current) : {};
      const inputSize: any = inputRef.current ? getSize(inputRef.current) : {};
      logger.log({ inputOffset, inputSize, oRect: oRect?.toJSON() });
      setToolbarStyle({
        left: Math.min(oRect?.left || 0, width - 135),
        top: (oRect?.top || 0) + window.scrollY - 40,
        display: "flex",
        position: "absolute",
      });
    } else {
      setToolbarStyle({ display: "none" });
    }
  };

  const onPaste = (event: ClipboardEvent | any) => {
    event.stopPropagation();
    event.preventDefault();
    const clipboardData = event.clipboardData;
    const pastedData = clipboardData?.getData("Text") || "";
    logger.log({ event, clipboardData, pastedData });
    if (inputRef.current) {
      inputRef.current.innerText += pastedData;
      inputRef.current.blur();
      setTimeout(() => {
        inputRef.current && inputRef.current.focus();
      }, 50);
    }
  };

  const onInputBlur = () => {
    setTimeout(() => {
      setToolbarStyle({ display: "none" });
    }, 150);
  };

  const submitComment = () => {
    const comment = inputRef.current?.innerHTML || "";
    const quoteCommentId = quotedComment?.commentId;
    initializeTaskComment({ taskId, comment, quoteCommentId });
  };

  const onKeyUp = (event: KeyboardEvent<HTMLDivElement>) => {
    //@ts-ignore
    const innerText = event.target?.innerText;
    const innerTextIsEmpty = innerText == "" || innerText == "\n";
    logger.log({ innerText });
    setRenderPlaceholder(innerTextIsEmpty);
  };

  const unquoteComment = () => {
    setQuotedComment?.(undefined);
  };

  return (
    <div id={"new-task-comment-input-container"} className={styles.container} onBlur={onInputBlur}>
      <div className={styles.content}>
        {quotedComment && (
          <div className={styles.quotedCommentContainer}>
            <div className={styles.unquoteCommentButtonContainer}>
              <Button
                className={styles.unquoteCommentButton}
                heightVariant={ButtonHeight.short}
                variant={ButtonVariants.filled2}
                onClick={unquoteComment}
              >
                <IoClose />
              </Button>
            </div>

            <CommentSimple comment={quotedComment} asQuoted={true} />
          </div>
        )}

        <div className={styles.commentText}>
          <div className={styles.newCommentContainer}>
            <div className={styles.inputContainer}>
              <div
                id={"new-task-comment-input"}
                ref={inputRef}
                className={styles.input}
                role="textbox"
                contentEditable={!isLoading}
                onSelect={onTextSelect}
                onPaste={onPaste}
                onKeyUp={onKeyUp}
              ></div>
              {renderPlaceholder && <div className={styles.placeHolder}>{t("taskCommentsInputPlaceholder")}</div>}

              <Button
                variant={ButtonVariants.filled2}
                heightVariant={ButtonHeight.short}
                onClick={submitComment}
                loading={isLoading}
                disabled={isLoading}
                className={styles.submitButton}
              >
                {t("taskCommentsCommentSubmit")}
              </Button>
            </div>
            <div className={styles.toolbar} style={{ ...toolbarStyle }}>
              <Button onClick={() => document.execCommand("bold", false)}>
                <FaBold />
              </Button>
              <Button onClick={() => document.execCommand("italic", false)}>
                <FaItalic />
              </Button>
              <Button onClick={() => document.execCommand("formatBlock", false, "h1")}>
                <FaHeading />
              </Button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CommentInput;
