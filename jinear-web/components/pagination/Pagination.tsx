import cn from "classnames";
import React, { ChangeEvent } from "react";
import { IoCaretBack, IoCaretForward } from "react-icons/io5";
import Button from "../button";
import PageInfo from "../pageInfo/PageInfo";
import styles from "./Pagination.module.css";

interface PaginationProps {
  id: string;
  className: string;
  pageNumber: number;
  pageSize: number;
  totalPages: number;
  totalElements: number;
  hasPrevious: boolean;
  hasNext: boolean;
  isLoading: boolean;
  page: number;
  setPage: React.Dispatch<React.SetStateAction<number>>;
}

const Pagination: React.FC<PaginationProps> = ({
  id,
  className,
  pageNumber,
  pageSize,
  totalPages,
  totalElements,
  hasPrevious,
  hasNext,
  isLoading,
  page,
  setPage,
}) => {
  const onSelectChange = (e: ChangeEvent<HTMLSelectElement>) => {
    const newPage = parseInt(e.target.value) - 1;
    setPage(newPage);
  };

  return (
    <div id={id} className={cn(styles.paginationContainer, className)}>
      <PageInfo
        number={pageNumber}
        size={pageSize}
        totalElements={totalElements}
      />
      <div className={styles.buttonContainer}>
        <Button
          disabled={isLoading || !hasPrevious}
          //   variant={ButtonVariants.filled}
          onClick={() => {
            setPage(page - 1);
          }}
        >
          <IoCaretBack />
        </Button>
        <select onChange={onSelectChange} value={pageNumber + 1}>
          {Array.from(Array(totalPages).keys()).map((i) => (
            <option key={`${id}-pagination-option-${i}`}>{i + 1}</option>
          ))}
        </select>
        <Button
          disabled={isLoading || !hasNext}
          //   variant={ButtonVariants.filled}
          onClick={() => {
            setPage(page + 1);
          }}
        >
          <IoCaretForward />
        </Button>
      </div>
    </div>
  );
};

export default Pagination;
