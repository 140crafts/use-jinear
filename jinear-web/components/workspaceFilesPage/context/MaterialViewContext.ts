import { createContext, useContext } from "react";
import { MaterialDto } from "@/be/jinear-core";

interface IMaterialViewContext {
  cdFolder: (materialId?: string) => void;
  selectedMaterial?: MaterialDto,
  setSelectedMaterial: (material?: MaterialDto) => void,
  resetList: () => void;
  dragOverMaterialId?: string,
  setDragOverMaterialId: (materialId?: string) => void;
  isInRoot?: boolean;
  isInRecent?: boolean;
  isInImages?: boolean;
  isInDocuments?: boolean;
  isInShared?: boolean;
}

const MaterialViewContext = createContext<IMaterialViewContext>({
  cdFolder: (materialId?: string) => {
  },
  selectedMaterial: undefined,
  setSelectedMaterial: (material?: MaterialDto) => {
  },
  resetList: () => {
  },
  dragOverMaterialId: undefined,
  setDragOverMaterialId: (materialId?: string) => {
  }
});

export default MaterialViewContext;

export function useCdFolder() {
  const ctx = useContext(MaterialViewContext);
  return ctx.cdFolder;
}

export function useResetList() {
  const ctx = useContext(MaterialViewContext);
  return ctx.resetList;
}

export function useDragOverMaterialId() {
  const ctx = useContext(MaterialViewContext);
  return ctx.dragOverMaterialId;
}

export function useSetDragOverMaterialId() {
  const ctx = useContext(MaterialViewContext);
  return ctx.setDragOverMaterialId;
}

export function useSelectedMaterial() {
  const ctx = useContext(MaterialViewContext);
  return ctx.selectedMaterial;
}

export function useSelectedMaterialId() {
  const ctx = useContext(MaterialViewContext);
  return ctx.selectedMaterial?.materialId;
}

export function useSetSelectedMaterial() {
  const ctx = useContext(MaterialViewContext);
  return ctx.setSelectedMaterial;
}

export function useIsInRoot() {
  const ctx = useContext(MaterialViewContext);
  return ctx.isInRoot;
}

export function useIsInRecent() {
  const ctx = useContext(MaterialViewContext);
  return ctx.isInRecent;
}

export function useIsInImages() {
  const ctx = useContext(MaterialViewContext);
  return ctx.isInImages;
}

export function useIsInDocuments() {
  const ctx = useContext(MaterialViewContext);
  return ctx.isInDocuments;
}

export function useIsInShared() {
  const ctx = useContext(MaterialViewContext);
  return ctx.isInShared;
}