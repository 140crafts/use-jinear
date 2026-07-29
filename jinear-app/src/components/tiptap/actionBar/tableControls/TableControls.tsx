import Button, {ButtonVariants} from "@/components/button";
import useTranslation from "@/locals/useTranslation";
import {Editor, useEditorState} from "@tiptap/react";
import React from "react";
import {LuColumns2, LuRows2, LuTable, LuTrash2} from "react-icons/lu";
import {
    RiDeleteColumn,
    RiDeleteRow,
    RiInsertColumnLeft,
    RiInsertColumnRight,
    RiInsertRowBottom,
    RiInsertRowTop,
    RiMergeCellsHorizontal,
    RiSplitCellsHorizontal
} from "react-icons/ri";
import styles from "./TableControls.module.css";

interface TableControlsProps {
    editor: Editor;
}

const NEW_TABLE = {rows: 3, cols: 3, withHeaderRow: true};

/**
 * Table buttons for the editor {@link ActionBar}. The insert button shows only outside a table —
 * prosemirror-tables does not support nested tables — and the editing group only inside one.
 */
const TableControls: React.FC<TableControlsProps> = ({editor}) => {
    const {t} = useTranslation();

    // `useEditor` does not re-render on transactions in TipTap v3, so caret-dependent state has to
    // be subscribed to explicitly — otherwise `isActive`/`can` here would be whatever they were on
    // the last parent render. Outside a table the `&&` short-circuits every `can()` but one.
    const tableState = useEditorState({
        editor,
        selector: ({editor}) => {
            const inTable = editor.isActive("table");
            return {
                inTable,
                canInsertTable: !inTable && editor.can().chain().focus().insertTable(NEW_TABLE).run(),
                canAddColumnBefore: inTable && editor.can().chain().focus().addColumnBefore().run(),
                canAddColumnAfter: inTable && editor.can().chain().focus().addColumnAfter().run(),
                canDeleteColumn: inTable && editor.can().chain().focus().deleteColumn().run(),
                canAddRowBefore: inTable && editor.can().chain().focus().addRowBefore().run(),
                canAddRowAfter: inTable && editor.can().chain().focus().addRowAfter().run(),
                canDeleteRow: inTable && editor.can().chain().focus().deleteRow().run(),
                canMergeCells: inTable && editor.can().chain().focus().mergeCells().run(),
                canSplitCell: inTable && editor.can().chain().focus().splitCell().run(),
                canToggleHeaderRow: inTable && editor.can().chain().focus().toggleHeaderRow().run(),
                canToggleHeaderColumn: inTable && editor.can().chain().focus().toggleHeaderColumn().run(),
                canDeleteTable: inTable && editor.can().chain().focus().deleteTable().run()
            };
        }
    });

    if (!tableState) return null;

    return tableState.inTable ? (
        <>
            <span className={styles.divider}/>
            <Button
                onClick={() => editor.chain().focus().addColumnBefore().run()}
                disabled={!tableState.canAddColumnBefore}
                variant={ButtonVariants.hoverFilled}
                data-tooltip-top={t("textEditorAddColumnBefore")}
            >
                <RiInsertColumnLeft/>
            </Button>
            <Button
                onClick={() => editor.chain().focus().addColumnAfter().run()}
                disabled={!tableState.canAddColumnAfter}
                variant={ButtonVariants.hoverFilled}
                data-tooltip-top={t("textEditorAddColumnAfter")}
            >
                <RiInsertColumnRight/>
            </Button>
            <Button
                onClick={() => editor.chain().focus().deleteColumn().run()}
                disabled={!tableState.canDeleteColumn}
                variant={ButtonVariants.hoverFilled}
                data-tooltip-top={t("textEditorDeleteColumn")}
            >
                <RiDeleteColumn/>
            </Button>
            <Button
                onClick={() => editor.chain().focus().addRowBefore().run()}
                disabled={!tableState.canAddRowBefore}
                variant={ButtonVariants.hoverFilled}
                data-tooltip-top={t("textEditorAddRowBefore")}
            >
                <RiInsertRowTop/>
            </Button>
            <Button
                onClick={() => editor.chain().focus().addRowAfter().run()}
                disabled={!tableState.canAddRowAfter}
                variant={ButtonVariants.hoverFilled}
                data-tooltip-top={t("textEditorAddRowAfter")}
            >
                <RiInsertRowBottom/>
            </Button>
            <Button
                onClick={() => editor.chain().focus().deleteRow().run()}
                disabled={!tableState.canDeleteRow}
                variant={ButtonVariants.hoverFilled}
                data-tooltip-top={t("textEditorDeleteRow")}
            >
                <RiDeleteRow/>
            </Button>
            <Button
                onClick={() => editor.chain().focus().mergeCells().run()}
                disabled={!tableState.canMergeCells}
                variant={ButtonVariants.hoverFilled}
                data-tooltip-top={t("textEditorMergeCells")}
            >
                <RiMergeCellsHorizontal/>
            </Button>
            <Button
                onClick={() => editor.chain().focus().splitCell().run()}
                disabled={!tableState.canSplitCell}
                variant={ButtonVariants.hoverFilled}
                data-tooltip-top={t("textEditorSplitCell")}
            >
                <RiSplitCellsHorizontal/>
            </Button>
            <Button
                onClick={() => editor.chain().focus().toggleHeaderRow().run()}
                disabled={!tableState.canToggleHeaderRow}
                variant={ButtonVariants.hoverFilled}
                data-tooltip-top={t("textEditorToggleHeaderRow")}
            >
                <LuRows2/>
            </Button>
            <Button
                onClick={() => editor.chain().focus().toggleHeaderColumn().run()}
                disabled={!tableState.canToggleHeaderColumn}
                variant={ButtonVariants.hoverFilled}
                data-tooltip-top={t("textEditorToggleHeaderColumn")}
            >
                <LuColumns2/>
            </Button>
            <Button
                onClick={() => editor.chain().focus().deleteTable().run()}
                disabled={!tableState.canDeleteTable}
                variant={ButtonVariants.hoverFilled}
                data-tooltip-top={t("textEditorDeleteTable")}
            >
                <LuTrash2/>
            </Button>
        </>
    ) : (
        <Button
            onClick={() => editor.chain().focus().insertTable(NEW_TABLE).run()}
            disabled={!tableState.canInsertTable}
            variant={ButtonVariants.hoverFilled}
            data-tooltip-top={t("textEditorInsertTable")}
        >
            <LuTable/>
        </Button>
    );
};

export default TableControls;
